"use client";

import { Post } from "@/types/post";
import { useRouter } from "next/navigation";

import { useEffect, useState } from "react";
export default function ClientPage({ id }: { id: string }) {
  const router = useRouter();
  const [post, setPost] = useState<Post | null>(null);

  console.log("실행됨 1");

  useEffect(() => {
    console.log("실행됨 2");
    fetch(`http://localhost:8080/api/v1/posts/${id}`, {
      credentials: "include",
    })
      .then((res) => {
        if (!res.ok) throw new Error("권한이 없습니다.");

        return res;
      })
      .then((res) => res.json())
      .then((data) => setPost(data))
      .catch((err) => {
        alert(err.message);
        router.back();
      });
  }, []);

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const formData = new FormData(e.target as HTMLFormElement);
    const title = formData.get("title") as string;
    const body = formData.get("body") as string;

    await fetch(`http://localhost:8080/api/v1/posts/${id}`, {
      credentials: "include",
      method: "PUT",
      body: JSON.stringify({ title, body }),
      headers: {
        "Content-Type": "application/json",
      },
    }).then((data) => data.json());

    alert("수정되었습니다.");

    router.back();
  };

  return (
    <div>
      {post && (
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            name="title"
            placeholder="제목"
            defaultValue={post.title}
          />
          <textarea name="body" placeholder="내용" defaultValue={post.body} />
          <button type="submit">수정</button>
        </form>
      )}
    </div>
  );
}
