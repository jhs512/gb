import type { Metadata } from "next";
import localFont from "next/font/local";
import "./globals.css";
import Link from "next/link";

const pretendard = localFont({
  src: "../../node_modules/pretendard/dist/web/variable/woff2/PretendardVariable.woff2",
  display: "swap",
  weight: "45 920",
  variable: "--font-pretendard",
});

export const metadata: Metadata = {
  title: "GB",
  description: "개발자 블로그 솔루션",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko" className={`${pretendard.variable}`}>
      <body className={pretendard.className}>
        <header>
          <div className="flex gap-2">
            <Link href="/">GB</Link>
            <Link href="/member/login">로그인</Link>
            <Link href="/p/list">글 목록</Link>
          </div>
        </header>
        <main>{children}</main>
      </body>
    </html>
  );
}
