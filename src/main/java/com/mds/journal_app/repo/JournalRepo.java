package com.mds.journal_app.repo;

import com.mds.journal_app.pojo.Journal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepo extends MongoRepository<Journal, String> {
}
