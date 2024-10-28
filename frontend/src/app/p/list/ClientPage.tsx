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
  const page = searchParams.get("page") ?? "1";

  useEffect(() => {
    fetch(`http://localhost:8080/api/v1/posts?page=${page}`)
      .then((res) => res.json())
      .then((data) => setPostPage(data));
  }, [page]);

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

      <div>
        {postPage && (
          <div className="flex gap-2">
            {/* postPage.totalPages 로 반복문 */}
            {Array.from({ length: postPage.totalPages }).map((_, index) => (
              <Link href={`/p/list?page=${index + 1}`} key={index}>
                {index + 1}
              </Link>
            ))}
          </div>
        )}
      </div>
    </>
  );
}
