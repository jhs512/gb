"use client";

import { Page } from "@/app/types/Page";
import { Post } from "@/app/types/Post";
import Link from "next/link";
import { useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";
export default function ClientPage() {
  const [postPage, setPostPage] = useState<Page<Post> | null>(null);

  // 여기서 쿼리 파라미터 받아오기
  const searchParams = useSearchParams();
  const currentPageNumber = parseInt(searchParams.get("page") ?? "1");

  useEffect(() => {
    fetch(`http://localhost:8080/api/v1/posts?page=${currentPageNumber}`)
      .then((res) => res.json())
      .then((data) => setPostPage(data));
  }, [currentPageNumber]);

  // 현재 페이지 기준으로 좌우 몇 페이지까지 보여줄지
  const pageMenuArmSize = 5;

  return (
    <>
      <div className="grid">
        {postPage && (
          <>
            {postPage.content.map((post) => (
              <Link href={`/p/${post.id}`} key={post.id}>
                {post.author.nickname}
                {post.title}
              </Link>
            ))}
          </>
        )}
      </div>

      <Link href="/p/write">글쓰기</Link>

      <div className="flex gap-2">
        {postPage && currentPageNumber - pageMenuArmSize > 1 && (
          <>
            <Link href={`/p/list?page=1`}>1</Link>
            <div>...</div>
          </>
        )}
        {postPage &&
          Array.from({ length: 100 }, (_, i) => i + 1)
            .filter((pageNumber) => {
              const start = currentPageNumber - pageMenuArmSize;

              const end = Math.min(
                currentPageNumber + pageMenuArmSize,
                postPage.totalPages
              );

              return pageNumber >= start && pageNumber <= end;
            })
            .map((pageNumber) => (
              <Link
                href={`/p/list?page=${pageNumber}`}
                key={pageNumber}
                className={
                  currentPageNumber === pageNumber ? "text-red-500" : ""
                }
              >
                {pageNumber}
              </Link>
            ))}
        {postPage &&
          currentPageNumber + pageMenuArmSize < postPage.totalPages && (
            <>
              <div>...</div>
              <Link href={`/p/list?page=${postPage.totalPages}`}>
                {postPage.totalPages}
              </Link>
            </>
          )}
      </div>
    </>
  );
}
