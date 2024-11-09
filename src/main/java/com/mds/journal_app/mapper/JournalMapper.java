package com.mds.journal_app.mapper;


import com.mds.journal_app.dao.Journal;
import com.mds.journal_app.pojo.JournalResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel ="spring")
public interface JournalMapper {
    JournalResponse toJournalResponse(Journal journal);
}
