'use client';

import { useCallback, useEffect, useRef, useState } from 'react';

import Spot from './Spot';
import Spinner from '../../ui/Spinner';

import { getSpots } from '@/app/(with-header)/place/[id]/@reviews/action';

import type { TSpot } from '@/types/response';

export default function Spots({
  placeId,
  initialPage,
  initialSpots,
  initialHasNext,
}: {
  placeId: number;
  initialPage: number;
  initialSpots: TSpot[];
  initialHasNext: boolean;
}) {
  const endRef = useRef<HTMLDivElement>(null);

  const [spots, setSpots] = useState(initialSpots);
  const [currentPage, setCurrentPage] = useState(initialPage);
  const [hasNext, setHasNext] = useState(initialHasNext);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  const onScroll: IntersectionObserverCallback = useCallback(
    async (entries) => {
      if (!entries[0].isIntersecting) {
        return;
      }

      setError('');
      setIsLoading(true);

      const nextPage = currentPage + 1;

      const result = await getSpots(placeId, nextPage);

      if (!result.success) {
        setError('리뷰를 가져올 수 없습니다!');
        return;
      }

      setHasNext(result.data.has_next);
      setSpots([...spots, ...result.data.spots]);
      setCurrentPage(nextPage);
      setIsLoading(false);
    },
    [currentPage, spots, placeId],
  );

  useEffect(() => {
    if (!endRef.current) {
      return;
    }

    const observer = new IntersectionObserver(onScroll, { threshold: 0.9 });
    observer.observe(endRef.current);

    if (!hasNext) {
      observer.unobserve(endRef.current);
    }

    return () => {
      observer.disconnect();
    };
  }, [endRef, hasNext, onScroll]);

  if (spots.length === 0) {
    return (
      <section className="w-full aspect-square flex flex-col justify-center items-center text-center">
        <div className="flex flex-col gap-6">
          <span className="text-6xl">🍃</span>
          <span className="text-2xl break-keep">
            장소에 대한 리뷰가 없습니다.
          </span>
        </div>
      </section>
    );
  }

  return (
    <section className="pt-4">
      {spots.map((spot, index) => (
        <Spot placeId={placeId} key={index} data={spot} />
      ))}
      {error}
      <div
        className="mt-2 min-h-6 bg-gray-200 flex justify-center items-center"
        ref={endRef}
      >
        <div className="py-4">
          {hasNext ? (
            isLoading ? (
              <Spinner />
            ) : (
              <span className="font-medium">아래로 내려 더 보기</span>
            )
          ) : (
            <span className="font-medium">마지막 결과입니다!</span>
          )}
        </div>
      </div>
    </section>
  );
}
