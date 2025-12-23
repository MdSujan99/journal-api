package com.mds.journal_app.dao;

import com.mds.journal_app.domain.Journal;
import com.mds.journal_app.domain.JournalRepoSQL;
import com.mds.journal_app.domain.JournalSQL;
import com.mds.journal_app.mapper.JournalMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JournalDaoImplSQL implements JournalDao {
  private final JournalRepoSQL journalRepo;
  private final JournalMapper journalMapper;

  @Override
  public void saveJournal(Journal journal) {
    JournalSQL entity = journalMapper.toSqlEntity(journal);
    journalRepo.save(entity);
  }

  @Override
  public Journal findById(String journalId) {
    return journalMapper.fromSqlEntityToJournal(journalRepo.findById(journalId).orElse(null));
  }

  @Override
  public List<Journal> findAll() {
    return journalMapper.fromSqlEntityToJournalList(journalRepo.findAll());
  }

  @Override
  public void deleteById(String journalId) {
    journalRepo.deleteById(journalId);
  }
}
