package com.mds.journal_app.domain;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JournalRepoMongo extends MongoRepository<JournalMongo, String> {}
