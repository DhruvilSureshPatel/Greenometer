package com.example.daos;

import com.example.models.TestModel;
import com.google.inject.Inject;
import org.jdbi.v3.core.Jdbi;

import java.util.Optional;

public class TestDAO {

    private final Jdbi jdbi;

    @Inject
    public TestDAO(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public Optional<Long> create(TestModel testModel) {
        return jdbi.withHandle(handle -> handle.createUpdate("INSERT INTO test values (:id, :name)")
                .bind("id", testModel.getId())
                .bind("name", testModel.getName())
                .executeAndReturnGeneratedKeys()
                .mapTo(Long.class).findFirst()
        );
    }

    public TestModel get(long id) {
        return jdbi.withHandle(handle -> handle.createQuery("select * from test where id = :id")
                .bind("id", id).mapTo(TestModel.class).findFirst().orElse(null)
        );
    }
}

//@RegisterRowMapper(TestDAO.ActivationExecutionsRowMapper.class)
//public interface TestDAO {
//
//    @SqlUpdate("INSERT INTO test values(:id, :name)")
//    @GetGeneratedKeys
//    long create(@Bind("id") long id, @Bind("name") String name);
//
//    @SqlQuery("select * from test where id = :id order by id desc limit 1")
//    TestModel get(@Bind("id") long id);
//
//    @Slf4j
//    class ActivationExecutionsRowMapper implements RowMapper<TestModel> {
//
//        @Override
//        public TestModel map(ResultSet rs, StatementContext ctx) {
//            try {
//                return TestModel.builder()
//                        .id(rs.getLong("id"))
//                        .name(rs.getString("name"))
//                        .build();
//            } catch (Exception e) {
//                log.error("test row mapping failed", e);
//                throw new RuntimeException(e.getMessage());
//            }
//        }
//    }
//
//
//}

