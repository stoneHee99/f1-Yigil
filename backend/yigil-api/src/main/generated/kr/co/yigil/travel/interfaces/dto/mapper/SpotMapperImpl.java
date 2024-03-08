package kr.co.yigil.travel.interfaces.dto.mapper;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import kr.co.yigil.travel.domain.spot.SpotCommand;
import kr.co.yigil.travel.domain.spot.SpotInfo;
import kr.co.yigil.travel.interfaces.dto.SpotDetailInfoDto;
import kr.co.yigil.travel.interfaces.dto.SpotInfoDto;
import kr.co.yigil.travel.interfaces.dto.request.SpotRegisterRequest;
import kr.co.yigil.travel.interfaces.dto.request.SpotUpdateRequest;
import kr.co.yigil.travel.interfaces.dto.response.MySpotInPlaceResponse;
import kr.co.yigil.travel.interfaces.dto.response.MySpotsResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-07T21:27:34+0900",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Oracle Corporation)"
)
@Component
public class SpotMapperImpl implements SpotMapper {

    private final DateTimeFormatter dateTimeFormatter_yyyy_MM_dd_0159776256 = DateTimeFormatter.ofPattern( "yyyy-MM-dd" );

    @Override
    public MySpotInPlaceResponse toMySpotInPlaceResponse(SpotInfo.MySpot mySpot) {
        if ( mySpot == null ) {
            return null;
        }

        MySpotInPlaceResponse mySpotInPlaceResponse = new MySpotInPlaceResponse();

        mySpotInPlaceResponse.setExists( mySpot.isExists() );
        mySpotInPlaceResponse.setRate( doubleToString( mySpot.getRate() ) );
        List<String> list = mySpot.getImageUrls();
        if ( list != null ) {
            mySpotInPlaceResponse.setImageUrls( new ArrayList<String>( list ) );
        }
        mySpotInPlaceResponse.setCreateDate( localDateTimeToString( mySpot.getCreateDate() ) );
        mySpotInPlaceResponse.setDescription( mySpot.getDescription() );

        return mySpotInPlaceResponse;
    }

    @Override
    public SpotDetailInfoDto toSpotDetailInfoDto(SpotInfo.Main spotInfoMain) {
        if ( spotInfoMain == null ) {
            return null;
        }

        SpotDetailInfoDto spotDetailInfoDto = new SpotDetailInfoDto();

        spotDetailInfoDto.setPlaceName( spotInfoMain.getPlaceName() );
        spotDetailInfoDto.setRate( doubleToString( spotInfoMain.getRate() ) );
        spotDetailInfoDto.setPlaceAddress( spotInfoMain.getPlaceAddress() );
        spotDetailInfoDto.setMapStaticImageFileUrl( spotInfoMain.getMapStaticImageFileUrl() );
        List<String> list = spotInfoMain.getImageUrls();
        if ( list != null ) {
            spotDetailInfoDto.setImageUrls( new ArrayList<String>( list ) );
        }
        spotDetailInfoDto.setCreateDate( localDateTimeToString( spotInfoMain.getCreateDate() ) );
        spotDetailInfoDto.setDescription( spotInfoMain.getDescription() );

        return spotDetailInfoDto;
    }

    @Override
    public SpotCommand.ModifySpotRequest toModifySpotRequest(SpotUpdateRequest request) {
        if ( request == null ) {
            return null;
        }

        SpotCommand.ModifySpotRequest.ModifySpotRequestBuilder modifySpotRequest = SpotCommand.ModifySpotRequest.builder();

        modifySpotRequest.id( request.getId() );
        modifySpotRequest.rate( request.getRate() );
        modifySpotRequest.description( request.getDescription() );
        modifySpotRequest.originalImages( originalSpotImageListToOriginalSpotImageList( request.getOriginalSpotImages() ) );
        modifySpotRequest.updatedImages( updateSpotImageListToUpdateSpotImageList( request.getUpdateSpotImages() ) );

        return modifySpotRequest.build();
    }

    @Override
    public SpotCommand.RegisterSpotRequest toRegisterSpotRequest(SpotRegisterRequest request) {
        if ( request == null ) {
            return null;
        }

        SpotCommand.RegisterSpotRequest.RegisterSpotRequestBuilder registerSpotRequest = SpotCommand.RegisterSpotRequest.builder();

        registerSpotRequest.registerPlaceRequest( spotRegisterRequestToRegisterPlaceRequest( request ) );
        List<MultipartFile> list = request.getFiles();
        if ( list != null ) {
            registerSpotRequest.files( new ArrayList<MultipartFile>( list ) );
        }
        registerSpotRequest.pointJson( request.getPointJson() );
        registerSpotRequest.title( request.getTitle() );
        registerSpotRequest.description( request.getDescription() );
        registerSpotRequest.rate( request.getRate() );

        return registerSpotRequest.build();
    }

    @Override
    public MySpotsResponseDto of(SpotInfo.MySpotsResponse mySpotsResponse) {
        if ( mySpotsResponse == null ) {
            return null;
        }

        MySpotsResponseDto.MySpotsResponseDtoBuilder mySpotsResponseDto = MySpotsResponseDto.builder();

        mySpotsResponseDto.content( spotListInfoListToSpotInfoList( mySpotsResponse.getContent() ) );
        mySpotsResponseDto.totalPages( mySpotsResponse.getTotalPages() );

        return mySpotsResponseDto.build();
    }

    @Override
    public MySpotsResponseDto.SpotInfo of(SpotInfo.SpotListInfo spotInfo) {
        if ( spotInfo == null ) {
            return null;
        }

        MySpotsResponseDto.SpotInfo.SpotInfoBuilder spotInfo1 = MySpotsResponseDto.SpotInfo.builder();

        spotInfo1.spotId( spotInfo.getSpotId() );
        spotInfo1.title( spotInfo.getTitle() );
        spotInfo1.rate( spotInfo.getRate() );
        spotInfo1.imageUrl( spotInfo.getImageUrl() );
        if ( spotInfo.getCreatedDate() != null ) {
            spotInfo1.createdDate( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( spotInfo.getCreatedDate() ) );
        }
        spotInfo1.isPrivate( spotInfo.getIsPrivate() );

        return spotInfo1.build();
    }

    @Override
    public SpotInfoDto toSpotInfoDto(SpotInfo.Main spotInfoMain) {
        if ( spotInfoMain == null ) {
            return null;
        }

        SpotInfoDto spotInfoDto = new SpotInfoDto();

        spotInfoDto.setId( spotInfoMain.getId() );
        List<String> list = spotInfoMain.getImageUrls();
        if ( list != null ) {
            spotInfoDto.setImageUrlList( new ArrayList<String>( list ) );
        }
        spotInfoDto.setDescription( spotInfoMain.getDescription() );
        spotInfoDto.setOwnerProfileImageUrl( spotInfoMain.getOwnerProfileImageUrl() );
        spotInfoDto.setOwnerNickname( spotInfoMain.getOwnerNickname() );
        spotInfoDto.setRate( new DecimalFormat( "#.#" ).format( spotInfoMain.getRate() ) );
        if ( spotInfoMain.getCreateDate() != null ) {
            spotInfoDto.setCreateDate( dateTimeFormatter_yyyy_MM_dd_0159776256.format( spotInfoMain.getCreateDate() ) );
        }
        spotInfoDto.setLiked( spotInfoMain.isLiked() );

        return spotInfoDto;
    }

    protected SpotCommand.OriginalSpotImage originalSpotImageToOriginalSpotImage(SpotUpdateRequest.OriginalSpotImage originalSpotImage) {
        if ( originalSpotImage == null ) {
            return null;
        }

        SpotCommand.OriginalSpotImage.OriginalSpotImageBuilder originalSpotImage1 = SpotCommand.OriginalSpotImage.builder();

        originalSpotImage1.imageUrl( originalSpotImage.getImageUrl() );
        originalSpotImage1.index( originalSpotImage.getIndex() );

        return originalSpotImage1.build();
    }

    protected List<SpotCommand.OriginalSpotImage> originalSpotImageListToOriginalSpotImageList(List<SpotUpdateRequest.OriginalSpotImage> list) {
        if ( list == null ) {
            return null;
        }

        List<SpotCommand.OriginalSpotImage> list1 = new ArrayList<SpotCommand.OriginalSpotImage>( list.size() );
        for ( SpotUpdateRequest.OriginalSpotImage originalSpotImage : list ) {
            list1.add( originalSpotImageToOriginalSpotImage( originalSpotImage ) );
        }

        return list1;
    }

    protected SpotCommand.UpdateSpotImage updateSpotImageToUpdateSpotImage(SpotUpdateRequest.UpdateSpotImage updateSpotImage) {
        if ( updateSpotImage == null ) {
            return null;
        }

        SpotCommand.UpdateSpotImage.UpdateSpotImageBuilder updateSpotImage1 = SpotCommand.UpdateSpotImage.builder();

        updateSpotImage1.imageFile( updateSpotImage.getImageFile() );
        updateSpotImage1.index( updateSpotImage.getIndex() );

        return updateSpotImage1.build();
    }

    protected List<SpotCommand.UpdateSpotImage> updateSpotImageListToUpdateSpotImageList(List<SpotUpdateRequest.UpdateSpotImage> list) {
        if ( list == null ) {
            return null;
        }

        List<SpotCommand.UpdateSpotImage> list1 = new ArrayList<SpotCommand.UpdateSpotImage>( list.size() );
        for ( SpotUpdateRequest.UpdateSpotImage updateSpotImage : list ) {
            list1.add( updateSpotImageToUpdateSpotImage( updateSpotImage ) );
        }

        return list1;
    }

    protected SpotCommand.RegisterPlaceRequest spotRegisterRequestToRegisterPlaceRequest(SpotRegisterRequest spotRegisterRequest) {
        if ( spotRegisterRequest == null ) {
            return null;
        }

        SpotCommand.RegisterPlaceRequest.RegisterPlaceRequestBuilder registerPlaceRequest = SpotCommand.RegisterPlaceRequest.builder();

        registerPlaceRequest.mapStaticImageFile( spotRegisterRequest.getMapStaticImageFile() );
        registerPlaceRequest.placeImageFile( spotRegisterRequest.getPlaceImageFile() );
        registerPlaceRequest.placeName( spotRegisterRequest.getPlaceName() );
        registerPlaceRequest.placeAddress( spotRegisterRequest.getPlaceAddress() );
        registerPlaceRequest.placePointJson( spotRegisterRequest.getPlacePointJson() );

        return registerPlaceRequest.build();
    }

    protected List<MySpotsResponseDto.SpotInfo> spotListInfoListToSpotInfoList(List<SpotInfo.SpotListInfo> list) {
        if ( list == null ) {
            return null;
        }

        List<MySpotsResponseDto.SpotInfo> list1 = new ArrayList<MySpotsResponseDto.SpotInfo>( list.size() );
        for ( SpotInfo.SpotListInfo spotListInfo : list ) {
            list1.add( of( spotListInfo ) );
        }

        return list1;
    }
}
