'use server';

import {
  myPageCourseListSchema,
  myPageSpotListSchema,
  mypageCourseDetailSchema,
  mypageSpotDetailSchema,
} from '@/types/myPageResponse';
import { revalidatePath, revalidateTag } from 'next/cache';
import { getBaseUrl } from '@/app/utilActions';
import { cookies } from 'next/headers';
import { TModifyDetail } from '../spot/SpotDetail';
import {
  cdnPathToRelativePath,
  dataUrlToBlob,
  getMIMETypeFromDataURI,
  parseResult,
} from '@/utils';
import { TPatchCourse } from '../course/types';
import { backendErrorSchema, postResponseSchema } from '@/types/response';

export const getMyPageSpots = async (
  pageNo: number = 1,
  size: number = 5,
  sortOrder: string = 'desc',
  selectOption: string = 'all',
) => {
  const BASE_URL = await getBaseUrl();
  const cookie = cookies().get('SESSION')?.value;
  const res = await fetch(
    `${BASE_URL}/v1/spots/my?page=${pageNo}&size=${size}&sortBy=${
      sortOrder !== 'rate'
        ? `created_at&sortOrder=${sortOrder}`
        : `rate&sortOrder=desc`
    }&selected=${selectOption}`,
    {
      headers: {
        Cookie: `SESSION=${cookie}`,
      },
    },
  );
  const spotList = await res.json();
  const parsedSpotList = parseResult(myPageSpotListSchema, spotList);
  return parsedSpotList;
};

export const deleteMySpot = async (spotId: number) => {
  const BASE_URL = await getBaseUrl();
  const cookie = cookies().get('SESSION')?.value;
  const res = await fetch(`${BASE_URL}/v1/spots/${spotId}`, {
    method: 'DELETE',
    headers: {
      Cookie: `SESSION=${cookie}`,
    },
  });

  if (res.ok) {
    revalidatePath('/mypage/my/travel/spot');
  }
};

export const getMyPageCourses = async (
  pageNo: number = 1,
  size: number = 5,
  sortOrder: string = 'desc',
  selectOption: string = 'all',
) => {
  const BASE_URL = await getBaseUrl();
  const cookie = cookies().get('SESSION')?.value;
  const res = await fetch(
    `${BASE_URL}/v1/courses/my?page=${pageNo}&size=${size}&sortBy=${
      sortOrder !== 'rate'
        ? `created_at&sortOrder=${sortOrder}`
        : `rate&sortOrder=desc`
    }&selected=${selectOption}`,
    {
      headers: {
        Cookie: `SESSION=${cookie}`,
      },
    },
  );
  const courseList = await res.json();
  const parsedCourseList = parseResult(myPageCourseListSchema, courseList); //
  return parsedCourseList;
};

export const deleteMyCourse = async (courseId: number) => {
  const BASE_URL = await getBaseUrl();
  const cookie = cookies().get('SESSION')?.value;
  const res = await fetch(`${BASE_URL}/v1/courses/${courseId}`, {
    method: 'DELETE',
    headers: {
      Cookie: `SESSION=${cookie}`,
    },
  });
  if (res.ok) {
    revalidatePath('/mypage/my/travel/course');
    return res;
  }
};

export const changeOnPublicMyTravel = async (travel_id: number) => {
  const BASE_URL = await getBaseUrl();
  const cookie = cookies().get('SESSION')?.value;
  const res = await fetch(`${BASE_URL}/v1/travels/change-on-public`, {
    method: 'PUT',
    headers: {
      Cookie: `SESSION=${cookie}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(travel_id),
  });
  if (res.ok) {
    revalidatePath('/mypage/my/travel', 'layout');
  }
};

export const changeOnPrivateMyTravel = async (travel_id: number) => {
  const BASE_URL = await getBaseUrl();
  const cookie = cookies().get('SESSION')?.value;
  const res = await fetch(`${BASE_URL}/v1/travels/change-on-private`, {
    method: 'PUT',
    headers: {
      Cookie: `SESSION=${cookie}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(travel_id),
  });
  if (res.ok) {
    revalidatePath('/mypage/my/travel', 'layout');
  }
};

export const getSpotDetail = async (spotId: number) => {
  const BASE_URL = await getBaseUrl();
  const res = await fetch(`${BASE_URL}/v1/spots/${spotId}`, {
    next: {
      tags: [`spotDetail/${spotId}`],
    },
  });
  const result = await res.json();
  const parsedSpotDetail = mypageSpotDetailSchema.safeParse(result);
  return parsedSpotDetail;
};

export const patchSpotDetail = async (
  spotId: number,
  modifiedData: TModifyDetail,
): Promise<{ status: 'succeed' } | { status: 'failed'; message: string }> => {
  const { id, description, rate, image_urls } = modifiedData;
  const formData = new FormData();

  const originalSpotImages = image_urls.filter((image) => {
    if (!image.uri.startsWith('data')) {
      return {
        imageUrl: image.uri,
        index: Number(image.filename),
      };
    }
  });

  const updateSpotImages = image_urls
    .map(({ uri, filename }, idx) => {
      if (uri.startsWith('data'))
        return new File([dataUrlToBlob(uri)], filename, {
          type: getMIMETypeFromDataURI(uri),
        });
    })
    .filter((i) => i !== undefined);

  const updateSpotImagesIdx = image_urls
    .map((image, idx) => {
      if (image.uri.startsWith('data')) return idx;
    })
    .filter((i) => i !== undefined);

  formData.append('id', id.toString());
  formData.append('description', description);
  formData.append('rate', rate);

  originalSpotImages.forEach((image, idx) => {
    formData.append(`originalSpotImages[${idx}].index`, image.filename);
    formData.append(
      `originalSpotImages[${idx}].imageUrl`,
      cdnPathToRelativePath(image.uri),
    );
  });

  updateSpotImages.forEach((image, idx) => {
    if (image) {
      formData.append(`updateSpotImages[${idx}].imageFile`, image);
    }
  });

  updateSpotImagesIdx.forEach((item, idx) => {
    if (item)
      formData.append(`updateSpotImages[${idx}].index`, item.toString());
  });

  const BASE_URL = await getBaseUrl();
  const cookie = cookies().get(`SESSION`)?.value;
  const res = await fetch(`${BASE_URL}/v1/spots/${spotId}`, {
    method: 'POST',
    headers: {
      Cookie: `SESSION=${cookie}`,
    },
    body: formData,
  });
  const result = await res.json();

  const error = backendErrorSchema.safeParse(result);

  if (error.success) {
    return { status: 'failed', message: error.data.message };
  }

  const parsedResult = postResponseSchema.safeParse(result);

  if (!parsedResult.success) {
    return { status: 'failed', message: 'zod 검증 에러입니다.' };
  }
  revalidateTag(`spotDetail/${spotId}`);
  return { status: 'succeed' };
};

export const getCourseDetail = async (courseId: number) => {
  const BASE_URL = await getBaseUrl();

  const res = await fetch(`${BASE_URL}/v1/courses/${courseId}`, {
    next: {
      tags: [`courseDetail/${courseId}`],
    },
  });
  const result = await res.json();
  const courseDetail = parseResult(mypageCourseDetailSchema, result);

  return courseDetail;
};

export const patchCourseDetail = async (
  courseId: number,
  modifiedData: TPatchCourse,
): Promise<{ status: 'succeed' } | { status: 'failed'; message: string }> => {
  const BASE_URL = await getBaseUrl();
  const cookie = cookies().get(`SESSION`)?.value;
  const {
    title,
    description,
    rate,
    spotIdOrder,
    spots,
    line_string_json,
    map_static_image_url,
  } = modifiedData;
  const formData = new FormData();
  formData.append('title', title);
  formData.append('description', description);
  formData.append('rate', rate.toString());
  line_string_json &&
    formData.append('lineStringJson', JSON.stringify(line_string_json));
  if (spotIdOrder)
    spotIdOrder.forEach((id) => formData.append('spotIdOrder', id.toString()));

  if (map_static_image_url && !map_static_image_url.includes('yigil')) {
    formData.append(
      'mapStaticImage',
      new File(
        [dataUrlToBlob(map_static_image_url)],
        `${title} 코스 이미지.png`,
        {
          type: 'image/png',
        },
      ),
    );
  }

  spots &&
    spots.forEach((spot, firstIdx) => {
      formData.append(
        `courseSpotUpdateRequests[${firstIdx}].id`,
        spot.id.toString(),
      );
      formData.append(
        `courseSpotUpdateRequests[${firstIdx}].rate`,
        spot.rate.toString(),
      );
      formData.append(
        `courseSpotUpdateRequests[${firstIdx}].description`,
        spot.description,
      );
    });

  const updateSpotImages =
    spots &&
    spots.map((spot, firstIdx) => {
      return spot.image_url_list
        .map((image, secondIdx) => {
          if (image.uri.startsWith('data')) {
            return new File([dataUrlToBlob(image.uri)], image.filename, {
              type: getMIMETypeFromDataURI(image.uri),
            });
          }
        })
        .filter((i) => i);
    });

  const originalSpotImages =
    spots &&
    spots.map((spot) => {
      return spot.image_url_list.map((image, idx) => {
        if (!image.uri.startsWith('data'))
          return { uri: image.uri, filename: image.filename };
      });
    });

  originalSpotImages &&
    originalSpotImages.forEach((originSpot, firstIdx) => {
      originSpot.forEach((image, secondIdx) => {
        if (image) {
          formData.append(
            `courseSpotUpdateRequests[${firstIdx}].originalSpotImages[${secondIdx}].imageUrl`,
            cdnPathToRelativePath(image.uri),
          );
          formData.append(
            `courseSpotUpdateRequests[${firstIdx}].originalSpotImages[${secondIdx}].index`,
            image.filename,
          );
        }
      });
    });

  updateSpotImages &&
    updateSpotImages.forEach((updatedSpot, firstIdx) => {
      updatedSpot.forEach((spot, secondIdx) => {
        if (spot) {
          formData.append(
            `courseSpotUpdateRequests[${firstIdx}].updateSpotImages[${secondIdx}].index`,
            (secondIdx + 1).toString(),
          );

          formData.append(
            `courseSpotUpdateRequests[${firstIdx}].updateSpotImages[${secondIdx}].imageFile`,
            spot,
          );
        }
      });
    });

  const res = await fetch(`${BASE_URL}/v1/courses/${courseId}`, {
    method: 'POST',
    headers: {
      Cookie: `SESSION=${cookie}`,
    },
    body: formData,
  });

  const result = await res.json();

  const error = backendErrorSchema.safeParse(result);

  if (error.success) {
    return { status: 'failed', message: error.data.message };
  }

  const parsedResult = postResponseSchema.safeParse(result);

  if (!parsedResult.success) {
    return { status: 'failed', message: 'zod 검증 에러입니다.' };
  }
  revalidateTag(`courseDetail/${courseId}`);
  return { status: 'succeed' };
};
