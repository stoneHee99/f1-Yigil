'use client';

import { useState } from 'react';
import Link from 'next/link';

import Places from './Places';
import RegionSelect from './RegionSelect';
import DummyPlace from './dummy/DummyPlace';

import type { TPlace, TRegion } from '@/types/response';

export default function RegionPlaces({
  regions,
  initialRegion,
  initialRegionPlaces,
  isLoggedIn,
  variant,
  more,
  carousel,
}: {
  regions: TRegion[];
  initialRegion?: TRegion;
  initialRegionPlaces: TPlace[];
  isLoggedIn: boolean;
  variant?: 'primary' | 'secondary';
  more?: boolean;
  carousel?: boolean;
}) {
  const [regionPlaces, setRegionPlaces] =
    useState<TPlace[]>(initialRegionPlaces);
  const [currentRegion, setCurrentRegion] = useState(initialRegion);

  function handleRegionChange(nextRegion: TRegion) {
    setCurrentRegion(nextRegion);
  }

  return (
    <section className="flex flex-col" aria-label="region-places">
      <div className="flex justify-between items-center pt-2">
        <span className="pl-4 text-3xl font-medium">관심 지역</span>
        {!more && (
          <Link className="pr-4" href={`places/regions/${currentRegion?.id}`}>
            더보기
          </Link>
        )}
      </div>
      <div className="pt-2 pl-4">
        <RegionSelect
          userRegions={regions}
          currentRegion={currentRegion}
          changeRegion={handleRegionChange}
          setRegionPlaces={setRegionPlaces}
        />
      </div>
      {regions.length === 0 ? (
        <div className="relative flex overflow-hidden px-4">
          <DummyPlace variant="secondary" />
          <DummyPlace variant="secondary" />
          <div className="absolute inset-0 bg-white/75 flex justify-center items-center"></div>
        </div>
      ) : (
        <Places
          data={regionPlaces}
          isLoggedIn={isLoggedIn}
          variant={variant}
          carousel={carousel}
        />
      )}
    </section>
  );
}
