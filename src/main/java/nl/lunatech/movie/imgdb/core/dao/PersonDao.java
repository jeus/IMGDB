package nl.lunatech.movie.imgdb.core.dao;

import nl.lunatech.movie.imgdb.core.pojo.domain.Person;

/**
 * @author alikhandani
 * @created 25/05/2022
 * @project imgdb
 */
public interface PersonDao {
    Person saveAndFlush(Person person);

    Person joinById(int mid, int pid, String relation);

    Integer getShortestPath(int firstPid, int secondPid);


}
