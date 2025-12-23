package com.mds.journal_app.mapper;

import com.mds.journal_app.domain.Journal;
import com.mds.journal_app.domain.JournalMongo;
import com.mds.journal_app.domain.JournalSQL;
import com.mds.journal_app.pojo.JournalRequest;
import com.mds.journal_app.pojo.JournalResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JournalMapper {
  JournalResponse toJournalResponse(Journal journal);

  JournalSQL toSqlEntity(Journal journal);

  JournalMongo toMongoEntity(Journal journal);

  Journal fromSqlEntityToJournal(JournalSQL journalSQL);

  List<Journal> fromSqlEntityToJournalList(List<JournalSQL> journalSqlList);

  Journal fromMongoEntityToJournal(JournalMongo journalMongo);

  List<Journal> fromMongoEntityToJournalList(List<JournalMongo> joururnalMongoList);

  Journal fromJournalReqToJournal(JournalRequest journalRequest);
}
