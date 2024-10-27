"use client";

import { Post } from "@/app/types/Post";
import Link from "next/link";
import { useEffect, useState } from "react";
export default function ClientPage() {
  const [posts, setPosts] = useState<Post[]>([]);

  useEffect(() => {
    fetch("http://localhost:8080/api/v1/posts")
      .then((res) => res.json())
      .then((data) => setPosts(data));
  }, []);

  return (
    <div className="grid">
      {posts.map((post) => (
        <Link href={`/p/${post.id}`} key={post.id}>
          {post.title}
        </Link>
      ))}
    </div>
  );
}
