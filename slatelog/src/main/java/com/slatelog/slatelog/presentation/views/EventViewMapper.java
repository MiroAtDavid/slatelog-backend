package com.slatelog.slatelog.presentation.views;

import com.slatelog.slatelog.domain.media.Media;
import com.slatelog.slatelog.domain.event.Event;
import com.slatelog.slatelog.presentation.views.Views.EventView;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

// MapStruct maps domain objects to presentation objects (views) and vice versa if needed.
// https://mapstruct.org/

// MapStruct is one of the best tools to map domain objects to presentation objects (views) and vice
// versa if needed.
// It's a compile time tool, so it will not affect the runtime performance.

@Mapper
public interface EventViewMapper {

    // In the mapper interface, we define a constant INSTANCE of the mapper interface
    EventViewMapper INSTANCE = Mappers.getMapper(EventViewMapper.class);

    // If the source and target field are not the same, we can use the @Mapping annotation
    @Mapping(source = "userId", target = "creatorId")

    // If the target field is a derived field,
    //   we can use the @Mapping annotation with an expression to execute a short code
    @Mapping(expression = "java(event.getLikes().size())", target = "likes")

    // If the target field is a derived field,
    //   we can use the @Mapping annotation with a qualifiedByName to call a method
    @Mapping(source = "medias", target = "thumb", qualifiedByName = "selectThumb")
    EventView toEventView(Event event);

    // The custom method to select the first media as the thumb
    @Named("selectThumb")
    static Media selectThumb(List<Media> medias) {
        // Gets the first media from the list of medias or null USING Java 8 Stream API
        // medias.stream().findFirst().orElse(null);

        // Gets the first media from the list of medias or null USING if-else
        if (medias != null && !medias.isEmpty()) {
            // return medias.getFirst();
            return medias.get(0);
        }
        return null;
    }

}
