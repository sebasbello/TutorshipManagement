/**
 * Name(s) of the programmer(s): María José Torres Igartua.
 * Date of creation: March 01, 2023.
 * Date of update: March 01, 2023.
 */
package academictutorshipmanagement.model.dao;

import academictutorshipmanagement.model.DatabaseConnection;
import academictutorshipmanagement.model.pojo.EducationalProgram;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EducationalProgramDAO {

    public static ArrayList<EducationalProgram> getEducationalProgramsByUser(String username) {
        ArrayList<EducationalProgram> educationalPrograms = new ArrayList<>();
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String query = "SELECT DISTINCT educationalProgram.*\n"
                + "FROM educationalprogram\n"
                + "INNER JOIN educationalProgramRole\n"
                + "ON educationalProgram.idEducationalProgram = educationalProgramRole.idEducationalProgram\n"
                + "WHERE educationalProgramRole.username = ?"
                + "ORDER BY name ASC";
        try (Connection connection = databaseConnection.open()) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                EducationalProgram educationalProgram = new EducationalProgram();
                educationalProgram.setIdEducationalProgram(resultSet.getInt("idEducationalProgram"));
                educationalProgram.setName(resultSet.getString("name"));
                educationalPrograms.add(educationalProgram);
            }
        } catch (SQLException exception) {
            educationalPrograms = null;
        } finally {
            databaseConnection.close();
        }
        return educationalPrograms;
    }

}