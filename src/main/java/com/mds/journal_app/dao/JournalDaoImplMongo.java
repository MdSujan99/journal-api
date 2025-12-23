package com.mds.journal_app.dao;

import com.mds.journal_app.domain.Journal;
import com.mds.journal_app.domain.JournalMongo;
import com.mds.journal_app.domain.JournalRepoMongo;
import com.mds.journal_app.mapper.JournalMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JournalDaoImplMongo implements JournalDao {
  private final JournalRepoMongo journalRepo;
  private final JournalMapper journalMapper;

  @Override
  public void saveJournal(Journal journal) {
    JournalMongo entity = journalMapper.toMongoEntity(journal);
    journalRepo.save(entity);
  }

  @Override
  public Journal findById(String journalId) {
    return journalMapper.fromMongoEntityToJournal(journalRepo.findById(journalId).orElse(null));
  }

  @Override
  public List<Journal> findAll() {
    return journalMapper.fromMongoEntityToJournalList(journalRepo.findAll());
  }

  @Override
  public void deleteById(String journalId) {
    journalRepo.deleteById(journalId);
  }
}
