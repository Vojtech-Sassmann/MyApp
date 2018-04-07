package cz.vectoun.myapp.api.facade;

import cz.vectoun.myapp.api.dto.CreateNoteGroupDTO;
import cz.vectoun.myapp.api.dto.NoteGroupDTO;

import java.util.List;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
public interface NoteGroupFacade {

    /**
     * Creates new note group for given user
     * @param group specify attributes for new note group
     * @param userId id of the owner of the new note group
     * @return newly created note group
     */
    NoteGroupDTO createNoteGroup(CreateNoteGroupDTO group, Long userId);

    /**
     * Deletes note group found by given id
     * @param id id of note group that will be deleted
     */
    void deleteNoteGroup(Long id);

    /**
     * Updates note group
     *
     * @param newVersion new version of note group
     * @return updated note group
     */
    NoteGroupDTO updateNoteGroup(NoteGroupDTO newVersion);

    /**
     * Finds all noteGroups for given user.
     *
     * @param userId user id
     * @return list of all NoteGroups of user with given id
     */
    List<NoteGroupDTO> getAllForUser(Long userId);
}
