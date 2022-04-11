package com.example.tournamentservice.models;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamType implements UserType {

    @Override
    public int[] sqlTypes() {
//        return new int[] {StringType.INSTANCE.sqlType(), StringType.INSTANCE.sqlType()};
        return new int[] {LongType.INSTANCE.sqlType(), LongType.INSTANCE.sqlType()};
    }

    @Override
    public Class<?> returnedClass() {
        return Team.class;
    }

    @Override
    public boolean equals(Object o, Object o1) throws HibernateException {
        if (o == null || o1 == null)
            return o1 == o;

        if (o.getClass() != Team.class || o1.getClass() != Team.class)
            return false;
        Team m = (Team) o, m1 = (Team) o1;
        List<Long> p = m.getPlayers(), p1 = m1.getPlayers();
        return p.size() == p1.size() && p.containsAll(p1);
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        List<Long> players = ((Team) o).getPlayers();
        players.sort(Long::compareTo);
        return players.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings, SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException, SQLException {
        ArrayList<Long> players = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            Long tmp = resultSet.getLong(strings[i]);
            if (tmp != 0)
                players.add(tmp);
        }

        if (players.size() > 1)
            players.sort(Long::compareTo);

        return players.isEmpty() ? null : new Team(players);
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement, Object o, int i, SharedSessionContractImplementor sharedSessionContractImplementor) throws HibernateException, SQLException {
        if (o == null) {
            preparedStatement.setNull(i++, LongType.INSTANCE.sqlType());
            preparedStatement.setNull(i, LongType.INSTANCE.sqlType());
        } else {
            List<Long> players = ((Team) o).getPlayers();
            preparedStatement.setLong(i++, players.get(0));
            if (players.size() > 1)
                preparedStatement.setLong(i, players.get(1));
            else
                preparedStatement.setNull(i, LongType.INSTANCE.sqlType());
        }
    }

    @Override
    public Object deepCopy(Object o) throws HibernateException {
        if (o == null)
            return null;
        List<Long> players = ((Team) o).getPlayers(), res = new ArrayList<>();
        res.add(players.get(0));
        if (players.size() > 1) {
            res.add(players.get(1));
            res.sort(Long::compareTo);
        }
        return new Team(res);
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object o) throws HibernateException {
        List<Long> players = ((Team) o).getPlayers();
        Long[] res = new Long[players.size()];
        for (int i = 0; i < res.length; i++)
            res[i] = players.get(i);
        return res;
    }

    @Override
    public Object assemble(Serializable serializable, Object o) throws HibernateException {
        Long[] players = (Long[]) serializable;
        return new Team(new ArrayList<>(Arrays.asList(players)));
    }

    @Override
    public Object replace(Object o, Object o1, Object o2) throws HibernateException {
        return o;
    }
}
