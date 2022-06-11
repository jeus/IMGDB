package nl.lunatech.movie.imgdb.core.dao;

import nl.lunatech.movie.imgdb.core.pojo.domain.Movie;
import nl.lunatech.movie.imgdb.core.pojo.domain.Person;
import org.neo4j.cypherdsl.core.*;
import org.neo4j.driver.types.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author alikhandani
 * @created 25/05/2022
 * @project imgdb
 */
@Component
public class JpaPersonDao implements PersonDao {

    @Autowired
    PersonRepository personRepository;

    @Override
    @Transactional
    public Person saveAndFlush(Person person) {
        Person person1 = personRepository.save(person);
        return person1;
    }

    @Override
    @Transactional
    public List<Person> saveAllAndFlush(List<Person> persons) {
        List<Person> movie1 = personRepository.saveAll(persons);
        return movie1;
    }

    @Override
    public Person joinById(int pid, int mid, String title) {
        return personRepository.findOne(getRelationShipStatment(pid, mid, title)).get();
    }


    /**
     * "MATCH (p1:Person { pid: $0 }),(p2:Person { pid: $1 }),
     * result = shortestPath((p1)-[:ACTED_IN*]-(p2))
     * WHERE length(result) > 1 RETURN length(result) - 2"
     */
    public Integer getShortestPath(int firstPid, int secondPid){
        return personRepository.getShortPath(firstPid,secondPid);
    }


    private Statement getRelationShipStatment(int pid, int mid, String role) {
        Node p = Cypher.node("Person").named("p");
        Node m = Cypher.node("Movie").named("m");
        Relationship r = p.relationshipTo(m, role);
        return Cypher.match(p).match(m)
                .where(p.property("pid").isEqualTo(Cypher.anonParameter(pid)))
                .and(m.property("mid").isEqualTo(Cypher.anonParameter(mid)))
                .merge(r)
                .returning(
                        p.getRequiredSymbolicName(),
                        r.getRequiredSymbolicName(),
                        m.getRequiredSymbolicName()
                ).build();
    }

}
