package com.mds.journal_app.dao;

import com.mds.journal_app.domain.Journal;
import java.util.List;

public interface JournalDao {
  void saveJournal(Journal journal);

  Journal findById(String journalId);

  List<Journal> findAll();

  void deleteById(String journalId);
}
