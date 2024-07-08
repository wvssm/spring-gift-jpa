package gift.repository.impls;

import gift.domain.Member;
import gift.repository.MemberRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MemberRepositoryImpl implements MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemberRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Member member){
        var sql = "INSERT INTO users(email, password) VALUES (?,?)";
        jdbcTemplate.update(sql, member.getEmail(), member.getPassword());
    }
    @Override
    public Optional<Member> findByPasswordAndEmail(String email, String password) {
        var sql = "SELECT * FROM users WHERE email = ? And password = ?";
        try {
            Member member = jdbcTemplate.queryForObject(
                    sql,
                    (resultSet, rowNum) -> new Member(
                            resultSet.getLong("id"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    )
                    ,email
                    ,password
            );
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        var sql = "SELECT * FROM users WHERE email = ?";
        try {
            Member member = jdbcTemplate.queryForObject(
                    sql,
                    (resultSet, rowNum) -> new Member(
                            resultSet.getLong("id"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    )
                    ,email
            );
            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}