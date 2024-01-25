import FloatingActionButton from '@/app/_components/FloatingActionButton';
import { PostList } from '@/app/_components/post';

import { postData } from '@/app/_components/post/constants';

export default async function HomePage() {
  return (
    <main className="max-w-full flex flex-col gap-8 relative">
      <PostList title="인기 장소" data={postData} />
      <PostList title="관심 지역 장소" data={postData} />
      <FloatingActionButton />
    </main>
  );
}
