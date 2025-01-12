import org.sql2o.Connection;
import org.sql2o.Sql2oException;

import java.util.List;

public class Monster {
    private String name;
    private int personId;
    private int id;

    public Monster(String name, int personId){
        this.name= name;
        this.personId= personId;
    }

    public String getName() {
        return name;
    }

    public int getPersonId() {
        return personId;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object otherMonster) {
        if(!(otherMonster instanceof Monster)){
            return false;
        } else {
            Monster newMonster = (Monster) otherMonster;
            return this.getName().equals(newMonster.getName()) &&
                    this.getPersonId() == newMonster.getPersonId();
        }
    }

    public void save(){
        try(Connection con = DB.sql2o.open()) {
            String sql = "INSERT INTO monsters (name, personId) VALUES (:name, :personId)";
            this.id =(int) con.createQuery(sql, true)
                    .addParameter("name", this.name)
                    .addParameter("personId", this.personId)
                    .executeUpdate()
                    .getKey();
        }
    }

    public static List<Monster> all() {
        String sql = "SELECT * FROM monsters";
        try(Connection con = DB.sql2o.open()){
            return con.createQuery(sql).executeAndFetch(Monster.class);
        }
    }

    public static Monster find(int id) {
        try(Connection con = DB.sql2o.open()){
            String sql = "SELECT FROM monsters where id=:id";
            Monster monster = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Monster.class);
            return monster;
        }
    }

    public static void clearAll() {
        String sql = "DELETE from monsters *";
        try (Connection con = DB.sql2o.open()) {
            con.createQuery(sql)
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }
}
