package cz.vectoun.myapp.api.facade;

import cz.vectoun.myapp.api.dto.CreateNoteGroupDTO;
import cz.vectoun.myapp.api.dto.NoteGroupDTO;
import cz.vectoun.myapp.api.dto.UpdateNoteGroupDTO;

import java.util.List;

/**
 * @author Vojtech Sassmann <vojtech.sassmann@gmail.com>
 */
public interface NoteGroupFacade {

    /**
     * Finds note group by id
     *
     * @param id id
     * @return NoteGroup
     */
    NoteGroupDTO findById(Long id);

    /**
     * Creates new note group for given user
     * @param group specify attributes for new note group
     * @return newly created note group
     */
    NoteGroupDTO createNoteGroup(CreateNoteGroupDTO group);

    /**
     * Deletes note group found by given id
     * @param id id of note group that will be deleted
     */
    void deleteNoteGroup(Long id);

    /**
     * Updates note group
     *
     * @param id id of note group
     * @param update new version of note group
     * @return updated note group
     */
    NoteGroupDTO updateNoteGroup(Long id, UpdateNoteGroupDTO update);

    /**
     * Finds all noteGroups for given user.
     *
     * @param userId user id
     * @return list of all NoteGroups of user with given id
     */
    List<NoteGroupDTO> getAllForUser(Long userId);
}
