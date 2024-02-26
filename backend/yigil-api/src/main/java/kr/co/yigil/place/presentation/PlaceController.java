//package kr.co.yigil.place.presentation;
//
//import kr.co.yigil.auth.Auth;
//import kr.co.yigil.auth.domain.Accessor;
//import kr.co.yigil.place.dto.response.PlaceMapStaticImageResponse;
//import kr.co.yigil.place.dto.response.RateResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/places")
//public class PlaceController {
//
//    private final PlaceService placeService;
//
//    @GetMapping("/rates")
//    public ResponseEntity<RateResponse> getMemberRate(
//            @RequestParam("place_id") Long placeId,
//            @Auth final Accessor accessor
//    ) {
//        RateResponse rateResponse = placeService.getMemberRate(placeId, accessor.getMemberId());
//        return ResponseEntity.ok().body(rateResponse);
//    }
//
//    @GetMapping("/static-image")
//    public ResponseEntity<PlaceMapStaticImageResponse> getPlaceStaticImageFile(
//            @RequestParam("name") String name,
//            @RequestParam("address") String address
//    ) {
//        PlaceMapStaticImageResponse placeInfoResponse = placeService.getPlaceStaticImage(name, address);
//        return ResponseEntity.ok().body(placeInfoResponse);
//    }
//}
