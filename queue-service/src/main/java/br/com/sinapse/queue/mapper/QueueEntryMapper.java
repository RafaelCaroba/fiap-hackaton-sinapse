package br.com.sinapse.queue.mapper;

import br.com.sinapse.queue.entity.QueueEntry;
import br.com.sinapse.queue.enums.QueuePriority;
import br.com.sinapse.shared.event.TriageCompletedEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QueueEntryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "calledAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "priority", source = "priority")
    QueueEntry toEntity(TriageCompletedEvent event);

    default QueuePriority mapPriority(String priority) {
        return QueuePriority.valueOf(priority);
    }
}
