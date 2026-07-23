package br.com.sinapse.queue.repository;

import br.com.sinapse.queue.entity.QueueEntry;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueEntryRepository extends JpaRepository<QueueEntry, UUID> {
}
