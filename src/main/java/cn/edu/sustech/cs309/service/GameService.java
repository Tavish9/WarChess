package cn.edu.sustech.cs309.service;

import cn.edu.sustech.cs309.dto.ArchiveDTO;
import cn.edu.sustech.cs309.dto.GameDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface GameService {
    List<ArchiveDTO> getArchives(String username) throws JsonProcessingException;

    GameDTO loadArchive(Integer archiveId) throws JsonProcessingException;

    GameDTO ini(String username1, String username2) throws JsonProcessingException;

    GameDTO update(Integer playerId) throws JsonProcessingException;

    GameDTO stepBack(Integer playerId, boolean current) throws JsonProcessingException;
}
