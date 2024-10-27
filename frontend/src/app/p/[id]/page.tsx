import ClientPage from "./ClientPage";

export default function Page({ params: { id } }: { params: { id: string } }) {
  return <ClientPage id={id} />;
}
