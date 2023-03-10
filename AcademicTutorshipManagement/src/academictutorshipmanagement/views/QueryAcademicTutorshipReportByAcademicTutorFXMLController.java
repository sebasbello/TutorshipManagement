/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package academictutorshipmanagement.views;

import academictutorshipmanagement.model.dao.AcademicPersonnelDAO;
import academictutorshipmanagement.model.dao.AcademicTutorshipReportDAO;
import academictutorshipmanagement.model.dao.SchoolPeriodDAO;
import academictutorshipmanagement.model.dao.StudentDAO;
import academictutorshipmanagement.model.pojo.AcademicPersonnel;
import academictutorshipmanagement.model.pojo.AcademicTutorshipReport;
import academictutorshipmanagement.model.pojo.SchoolPeriod;
import academictutorshipmanagement.model.pojo.Student;
import academictutorshipmanagement.utilities.Constants;
import academictutorshipmanagement.utilities.Utilities;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author sebtr
 */
public class QueryAcademicTutorshipReportByAcademicTutorFXMLController implements Initializable {

    @FXML
    private TableView<Student> studentsTableView;
    @FXML
    private TableColumn<?, ?> registrationNumberTableColumn;
    @FXML
    private TableColumn nameTableColumn;
    @FXML
    private TableColumn paternalSurnameTableColumn;
    @FXML
    private TableColumn maternalSurnameTableColumn;
    @FXML
    private TableColumn attendedByTableColumn;
    @FXML
    private TableColumn atRiskTableColumn;
    @FXML
    private TextField numberOfStudentsAtRiskTextField;
    @FXML
    private TextField numberOfStudentsAttendingTextField;
    @FXML
    private TextField academicTutorshipSessionDateTextField;
    @FXML
    private ComboBox<Integer> academicTutorshipSessionComboBox;
    @FXML
    private ComboBox<SchoolPeriod> schoolPeriodComboBox;
    @FXML
    private ComboBox<AcademicPersonnel> academicPersonnelComboBox;
    @FXML
    private TextArea generalCommentTextArea;
    @FXML
    private TextField educationalProgramTextField;

    private ObservableList<SchoolPeriod> schoolPeriods;
    private ObservableList<AcademicPersonnel> academicPersonnels;
    private ObservableList<Integer> academicTutorshipSessions;
    private ObservableList<Student> students;

    private ArrayList<AcademicTutorshipReport> academicTutorshipReports;
    
    private SchoolPeriod schoolPeriod;
    private AcademicPersonnel academicPersonnel;

    private int idSchoolPeriod;
    private int idAcademicPersonnel;
    private int idAcademicTutorshipSession;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        schoolPeriods = FXCollections.observableArrayList();
        academicPersonnels = FXCollections.observableArrayList();
        academicTutorshipSessions = FXCollections.observableArrayList();
        students = FXCollections.observableArrayList();
        academicTutorshipReports = new ArrayList<>();
        academicPersonnelComboBox.disableProperty().bind(schoolPeriodComboBox.valueProperty().isNull());
        academicTutorshipSessionComboBox.disableProperty().bind(academicPersonnelComboBox.valueProperty().isNull());
    }

    public void configureView(SchoolPeriod schoolPeriod, AcademicPersonnel academicPersonnel) {
        this.schoolPeriod = schoolPeriod;
        this.academicPersonnel = academicPersonnel;
        configureStudentsTableViewColumns();
        loadSchoolPeriods();
    }
    
    private void configureStudentsTableViewColumns() {
        registrationNumberTableColumn.setCellValueFactory(new PropertyValueFactory("registrationNumber"));
        nameTableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        paternalSurnameTableColumn.setCellValueFactory(new PropertyValueFactory("paternalSurname"));
        maternalSurnameTableColumn.setCellValueFactory(new PropertyValueFactory("maternalSurname"));
        attendedByTableColumn.setCellValueFactory(new PropertyValueFactory("attendedByCheckBox"));
        atRiskTableColumn.setCellValueFactory(new PropertyValueFactory("atRiskCheckBox"));
    }

    private void loadSchoolPeriods() {
        ArrayList<SchoolPeriod> schoolPeriodsResultSet = SchoolPeriodDAO.getSchoolPeriods();
        if (schoolPeriodsResultSet.isEmpty()) {
            Utilities.showAlert("No hay conexi??n con la base de datos.\n\n"
                    + "Por favor, int??ntelo m??s tarde.\n",
                    Alert.AlertType.ERROR);
        } else {
            schoolPeriods.addAll(schoolPeriodsResultSet);
            schoolPeriodComboBox.setItems(schoolPeriods);
            schoolPeriodComboBox.valueProperty().addListener((ObservableValue<? extends SchoolPeriod> observable, SchoolPeriod oldValue, SchoolPeriod newValue) -> {
                if (newValue != null) {
                    academicPersonnels.clear();
                    academicTutorshipSessions.clear();
                    academicTutorshipReports.clear();
                    students.clear();
                    clearTextField();
                    idSchoolPeriod = newValue.getIdSchoolPeriod();
                    loadAcademicPersonnel(idSchoolPeriod);
                }
            });
        }
    }

    private void loadAcademicPersonnel(int idSchoolPeriod) {
        ArrayList<AcademicPersonnel> academicPersonnelResulSet
                = AcademicPersonnelDAO.getAcademicPersonnelByRole(Constants.ACADEMIC_TUTOR_ID_ROLE,
                        academicPersonnel.getUser().getEducationalProgram().getIdEducationalProgram());
        if (academicPersonnelResulSet.isEmpty()) {
            Utilities.showAlert("No hay conexi??n con la base de datos.\n\n"
                    + "Por favor, int??ntelo m??s tarde.\n",
                    Alert.AlertType.ERROR);
        } else {
            academicPersonnels.addAll(academicPersonnelResulSet);
            academicPersonnelComboBox.setItems(academicPersonnels);
            academicPersonnelComboBox.valueProperty().addListener((ObservableValue<? extends AcademicPersonnel> observable, AcademicPersonnel oldValue, AcademicPersonnel newValue) -> {
                if (newValue != null) {
                    academicTutorshipSessions.clear();
                    academicTutorshipReports.clear();
                    students.clear();
                    idAcademicPersonnel = newValue.getIdAcademicPersonnel();
                    clearTextField();
                    loadacademicTutorshipSessions(idAcademicPersonnel, idSchoolPeriod);
                }
            });
        }
    }
    
    private void loadacademicTutorshipSessions(int idAcademicPersonnel, int idSchoolPeriod) {
        ArrayList<AcademicTutorshipReport> academicTutorshipReportsResultSet = AcademicTutorshipReportDAO.getAcademicTutorshipReports(idAcademicPersonnel, idSchoolPeriod);
        if (academicTutorshipReportsResultSet.isEmpty()) {
            Utilities.showAlert("No hay conexi??n con la base de datos.\n\n"
                    + "Por favor, int??ntelo m??s tarde.\n",
                    Alert.AlertType.ERROR);
        } else {
            academicTutorshipReports.addAll(academicTutorshipReportsResultSet);
            int index = 0;
            for (AcademicTutorshipReport academicTutorshipReport : academicTutorshipReportsResultSet) {
                academicTutorshipSessions.add(++index);
            }
            academicTutorshipSessionComboBox.setItems(academicTutorshipSessions);
            academicTutorshipSessionComboBox.valueProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
                if (newValue != null) {
                    clearTextField();
                    configureAcademicTutorshipReportInformation(academicTutorshipReports.get(newValue - 1));
                }
            });
        }
    }
    
    private void configureAcademicTutorshipReportInformation(AcademicTutorshipReport academicTutorshipReport) {
        numberOfStudentsAttendingTextField.setText(String.valueOf(academicTutorshipReport.getNumberOfStudentsAttending()));
        numberOfStudentsAtRiskTextField.setText(String.valueOf(academicTutorshipReport.getNumberOfStudentsAtRisk()));
        educationalProgramTextField.setText(academicPersonnel.getUser().getEducationalProgram().toString());
        academicTutorshipSessionDateTextField.setText(String.valueOf(academicTutorshipReport.getAcademicTutorship().getAcademicTutorshipSession()));
        generalCommentTextArea.setText(academicTutorshipReport.getGeneralComment());
        loadStudentsByAcademicTutorshipReport(academicTutorshipReport);
    }
    
    private void loadStudentsByAcademicTutorshipReport(AcademicTutorshipReport academicTutorshipReport) {
        ArrayList<Student> studentsResultSet = StudentDAO.getStudentsByAcademicTutorshipReport(academicTutorshipReport.getIdAcademicTutorshipReport());
        students.addAll(studentsResultSet);
        configureTableViewCheckBoxes();
        studentsTableView.setItems(students);
    }
    
    private void configureTableViewCheckBoxes() {
        students.forEach(student -> {
            student.getAttendedByCheckBox().setSelected(student.isAttendedBy());
            student.getAtRiskCheckBox().setSelected(student.isAtRisk());
        });
    }
    
    private void clearTextField() {
        numberOfStudentsAttendingTextField.clear();
        numberOfStudentsAtRiskTextField.clear();
        educationalProgramTextField.clear();
        academicTutorshipSessionDateTextField.clear();
        generalCommentTextArea.clear();
    }
    
    @FXML
    private void cancelButtonClick(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TutorialReportManagementMenuFXML.fxml"));
        try {
            Parent root = loader.load();
            TutorialReportManagementMenuFXMLController tutorialReportManagementMenuFXMLController = loader.getController();
            tutorialReportManagementMenuFXMLController.configureView(schoolPeriod, academicPersonnel);
            Scene mainMenuView = new Scene(root);
            Stage stage = (Stage) studentsTableView.getScene().getWindow();
            stage.setScene(mainMenuView);
            stage.setTitle("Gesti??n de reportes tutoriales.");
            stage.show();
        } catch (IOException exception) {
            System.err.println("The TutorialReportManagementMenuFXML.fxml' file could not be open. Please try again later.");
        }
    }

    @FXML
    private void printButtonClick(ActionEvent event) {
    }

    @FXML
    private void downloadButtonClick(ActionEvent event) {
    }

    @FXML
    private void queryAcademicProblemButtonClick(ActionEvent event) {
    }

}
