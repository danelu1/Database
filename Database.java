import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Comparator;

class Database {                                        // The "Database" class was created using the Singleton design pattern
	private static Database single_instance = null;    
	
	private List<Student> students;                     // The members of the class are declared private and only the getters 
	private List<Teacher> teachers;						// methods are provided
	private static int count = 0;
	
	private Database() {
		students = new ArrayList<>();
		teachers = new ArrayList<>();
	}
	
	public static Database getInstance() {				// Method for instantiating a "Database" object and for counting the
		if (single_instance == null) {					// the number of instantiations
			single_instance = new Database();
		}
		count++;
		return single_instance;
	}
	
	public void addTeachers(List<Teacher> teachers) {	// Method that adds all the teachers to the our list of teachers
		this.teachers.addAll(teachers);
	}
	
	public void addStudents(List<Student> students) {   // The same as the above method but this time for students
		this.students.addAll(students);
	}
	
	public List<Student> findAllStudents() {     		// Method that returns the list of students
		return students;
	}
	
	public List<Teacher> findAllTeachers() {			// Method that returns the list of teachers
		return teachers;
	}
	
	public List<Student> getStudentsBySubject(String materie) {		// Method that returns a list with the students that have
		List<Student> list = new ArrayList<>();						// the mentioned course assigned
		
		for (int i = 0; i < students.size(); i++) {
			if (students.get(i).getSituation().containsKey(materie)) {
				list.add(students.get(i));
			}
		}
		return list;
	}
	
	public List<Teacher> findTeachersBySubject(String materie) { 	// The same as the above method but this time for teachers
		List<Teacher> list = new ArrayList<>();
		
		for (int i = 0; i < teachers.size(); i++) {
			for (int j = 0; j < teachers.get(i).materii.size(); j++) {
				if (teachers.get(i).materii.get(j).equals(materie)) {
					list.add(teachers.get(i));
				}
			}
		}
		return list;
	}
	
	public List<Student> getStudentsByAverageGrade() {				// Method that returns the students sorted by their average grade
		List<Student> list = new ArrayList<>();
		
		Collections.sort(students, new Comparator<Student>() {
			public int compare(Student s1, Student s2) {
				return Float.compare(s1.averageGrade(), s2.averageGrade());
			}
		});
		
		for (int i = 0; i < students.size(); i++) {
			list.add(students.get(i));
		}
		return list;
	}
	
	public List<Student> getStudentByGradeForSubject(String materie) {	// Method that returns a list of students sorted by their grade
		List<Student> list = new ArrayList<>();							// for a certain course in ascending order
		
		for (int i = 0; i < students.size(); i++) {
			if (students.get(i).getSituation().containsKey(materie)) {
				list.add(students.get(i));
			}
		}
		
		Comparator<Student> cmp = new Comparator<Student>() {
			public int compare(Student s1, Student s2) {
				return Integer.compare(s1.getGradeForSubject(materie), s2.getGradeForSubject(materie));
			}
		};
		
		Collections.sort(list, cmp);
		
		return list;
	}
	
	public String urzica() {											// Special class made for my own amusement, don't take it serious
		String ans = "";
		Collections.sort(students, new Comparator<Student>() {
			public int compare(Student s1, Student s2) {
				return Float.compare(s1.averageGrade(), s2.averageGrade());
			}
		});
		
		ans += students.get(students.size() - 1).getName() + " " + students.get(students.size() - 1).getSurname() + " e urzica";
		return ans;
	}
	
	public static int getNumberOfInstances() {
		return count;
	}
}

class Student {															// "Student" class which is read-only and uses all the methods
	private String nume;												// from the "Database" class
	private String prenume;												// Members of the class are the "name", the "surname" and the 
	private LinkedHashMap<String, Integer> situatie;					// situation of the student
	
	public Student(Student s) {											// Copy constructor of the class 
		s.nume = nume;
		s.prenume = prenume;
		s.situatie = situatie;
	}
	
	public Student(String nume, String prenume, Map<String, Integer> map) {		// Constructor which creates a deep copy of the student's
		this.nume = nume;														// situation
		this.prenume = prenume;
		LinkedHashMap<String, Integer> aux = new LinkedHashMap<String, Integer>();
		String key;
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			key = it.next();
			aux.put(key, map.get(key));
		}
		this.situatie = aux;
	}
	
	public String getName() { 						//The next three getters are for the members of the class which are private
		return nume;
	}
	
	public String getSurname() {
		return prenume;
	}
	
	@SuppressWarnings("unchecked")
	public LinkedHashMap<String, Integer> getSituation() {
		return (LinkedHashMap<String, Integer>) situatie.clone();
	}
	
	public float averageGrade() {						// Method which calculates a student's average grade
		int sum = 0;
		for (Map.Entry<String, Integer> map : situatie.entrySet()) {
			sum += map.getValue();
		}
		return (float) sum / situatie.size();
	}
	
	public String toString() {								// The toString() method which deploys a student after my personal choice
		String ans = "";
		ans += "Student " + nume + " " + prenume + " has the grades and subjects :\n";
		for (Map.Entry<String, Integer> map : situatie.entrySet()) {
			ans += map.getKey() + " : " + map.getValue();
			ans += "\n";
		}
		return ans;
	}
	
	public int getGradeForSubject(String materie) {		// Method that gets the grade for a certain course of the student
		return situatie.get(materie);
	}
	
	public List<Teacher> getAllTeachers() {				// Methods implemented in the "Database" class but this time unmodifiable
		Database db = Database.getInstance();
		return Collections.unmodifiableList(db.findAllTeachers());
	}
	
	public List<Teacher> getTeacherBySubject(String materie) {
		Database db = Database.getInstance();
		return Collections.unmodifiableList(db.findTeachersBySubject(materie));
	}
	
	public List<Student> getAllStudents() {
		Database db = Database.getInstance();
		return Collections.unmodifiableList(db.findAllStudents());
	}
	
	public List<Student> getStudentsBySubject(String materie) {
		Database db = Database.getInstance();
		return Collections.unmodifiableList(db.getStudentsBySubject(materie));
	}
	
	public List<Student> getStudentsByAverageGrade() {
		Database db = Database.getInstance();
		return Collections.unmodifiableList(db.getStudentsByAverageGrade());
	}
	
	public List<Student> getStudentsByGradeForSubject(String materie) {
		Database db = Database.getInstance();
		return Collections.unmodifiableList(db.getStudentByGradeForSubject(materie));
	}
}

class Teacher {								// The "Teacher" class is as the "Student" class but instead of situation
	String nume;							// we have a list of courses assigned to each teacher
	String prenume;
	List<String> materii;
	
	public Teacher(String nume, String prenume, List<String> materii) {
		this.nume = nume;
		this.prenume = prenume;
		this.materii = materii;
	}
	
	public Teacher(Teacher t) {
		t.nume = nume;
		t.prenume = prenume;
		t.materii = materii;
	}
	
	public List<Teacher> getAllTeachers() {
		Database db = Database.getInstance();
		return db.findAllTeachers();
	}
	
	public List<Teacher> getTeachersBySubject(String nume) {
		Database db = Database.getInstance();
		return db.findTeachersBySubject(nume);
	}
	
	public List<Student> getAllStudents() {
		Database db = Database.getInstance();
		return db.findAllStudents();
	}
	
	public List<Student> getStudentsBySubject(String materie) {
		Database db = Database.getInstance();
		return db.getStudentsBySubject(materie);
	}
	
	public List<Student> getStudentsByAverageGrade() {
		Database db = Database.getInstance();
		return db.getStudentsByAverageGrade();
	}
	
	public List<Student> getStudentsByGradeForSubject(String materie) {
		Database db = Database.getInstance();
		return db.getStudentByGradeForSubject(materie);
	}
	
	public String toString() {
		String ans = "";
		ans += "Teacher " + nume + " " + prenume + " has the courses :\n";
		for (int i = 0; i < materii.size(); i++) {
			ans += materii.get(i) + " ";
			ans += "\n";
		}
		return ans;
	}
}

class Helpers {					// Class used to store different subjects, teachers and students
    public static final String PROGRAMMING_PARADIGMS = "Programming Paradigms";
    public static final String OBJECT_ORIENTED_PROGRAMMING = "Object Oriented Programming";
    public static final String DATA_STRUCTURES = "Data Structures";
    public static final String PARALLEL_AND_DISTRIBUTED_ALGORITHMS = "Parallel and Distributed Algorithms";
    public static final String COMPILERS = "Compilers";
    public static final String LOCAL_AREA_NETWORKS = "Local Area Networks";
    public static final String ALGORITHMS_DESIGN = "Algorithms Design";
    public static final String COMMUNICATION_PROTOCOLS = "Communication Protocols";
    public static final String OPERATING_SYSTEMS_USAGE = "Operating Systems Usage";
    public static final String LOGIC_DESIGN = "Logic Design";
    public static final String NUMERICAL_METHODS = "Numerical Methods";
    public static final String ALGORITHMS_ANALYSIS = "Algorithms Analysis";
    public static final String ASSEMBLY_LANGUAGE_PROGRAMMING = "Assembly Language Programming";
    public static final String FORMAL_LANGUAGES_AND_AUTOMATA = "Formal Languages and Automata";
    public static final String OPERATING_SYSTEMS = "Operating Systems";
    public static final String COMPUTER_SYSTEMS_ARCHITECTURE = "Computer Systems Architecture";
    public static final String COMPUTER_GRAPHICS = "Computer Graphics";
    public static final String WEB_PROGRAMMING = "Web Programming";
    public static final String COMPUTERS_PROGRAMMING = "Computers Programming";
    
	public static void addTeachers() {
		Database.getInstance().addTeachers(Arrays.asList(
            new Teacher("Alex", "Olteanu",
                Arrays.asList(OBJECT_ORIENTED_PROGRAMMING, DATA_STRUCTURES)),
            new Teacher("Mihnea", "Muraru",
                Arrays.asList(PROGRAMMING_PARADIGMS, COMPILERS)),
            new Teacher("Irina", "Mocanu",
                Arrays.asList(DATA_STRUCTURES, COMPUTER_GRAPHICS, FORMAL_LANGUAGES_AND_AUTOMATA)),
            new Teacher("Ciprian", "Dobre",
                Arrays.asList(COMMUNICATION_PROTOCOLS, PARALLEL_AND_DISTRIBUTED_ALGORITHMS, WEB_PROGRAMMING)),
            new Teacher("Lorina", "Negreanu",
                Arrays.asList(OBJECT_ORIENTED_PROGRAMMING, FORMAL_LANGUAGES_AND_AUTOMATA)),
            new Teacher("Florin", "Pop",
                Arrays.asList(COMPUTERS_PROGRAMMING, NUMERICAL_METHODS, COMMUNICATION_PROTOCOLS)),
            new Teacher("Traian", "Rebedea",
                Arrays.asList(COMPUTERS_PROGRAMMING, ALGORITHMS_ANALYSIS, ALGORITHMS_DESIGN)),
            new Teacher("Matei", "Popovici",
                Arrays.asList(ALGORITHMS_ANALYSIS, FORMAL_LANGUAGES_AND_AUTOMATA, PROGRAMMING_PARADIGMS)),
            new Teacher("Costin", "Chiru",
                Arrays.asList(LOGIC_DESIGN, ALGORITHMS_DESIGN)),
            new Teacher("Razvan", "Deaconescu",
                Arrays.asList(OPERATING_SYSTEMS_USAGE, ASSEMBLY_LANGUAGE_PROGRAMMING, OPERATING_SYSTEMS)),
            new Teacher("Voichita", "Iancu",
                Arrays.asList(COMPUTER_SYSTEMS_ARCHITECTURE, ASSEMBLY_LANGUAGE_PROGRAMMING)),
            new Teacher("Mihai", "Carabas",
                Arrays.asList(OPERATING_SYSTEMS, OPERATING_SYSTEMS_USAGE)),
            new Teacher("Nicolae", "Tapus",
                Arrays.asList(LOCAL_AREA_NETWORKS, COMPUTER_SYSTEMS_ARCHITECTURE))
        ));
    }
    public static void addStudents() {
        Database.getInstance().addStudents(Arrays.asList(
            new Student(
                "Florin",
                "Mihalache",
                Map.of(
                    PROGRAMMING_PARADIGMS,10,
                    OBJECT_ORIENTED_PROGRAMMING, 9,
                    PARALLEL_AND_DISTRIBUTED_ALGORITHMS, 10,
                    COMPUTER_GRAPHICS, 8,
                    OPERATING_SYSTEMS_USAGE, 9,
                    COMPILERS, 9,
                    COMPUTER_SYSTEMS_ARCHITECTURE, 8)),
            new Student(
                "Andrei",
                "Mihu",
                Map.of(
                    PROGRAMMING_PARADIGMS,9,
                    OBJECT_ORIENTED_PROGRAMMING, 8,
                    COMMUNICATION_PROTOCOLS, 9,
                    COMPUTER_GRAPHICS, 8,
                    COMPUTER_SYSTEMS_ARCHITECTURE, 8,
                    OPERATING_SYSTEMS, 5,
                    LOCAL_AREA_NETWORKS, 7)),
            new Student(
                "George",
                "Mocanu",
                Map.of(
                    PROGRAMMING_PARADIGMS,10,
                    OBJECT_ORIENTED_PROGRAMMING, 10,
                    PARALLEL_AND_DISTRIBUTED_ALGORITHMS, 10,
                    COMPUTER_GRAPHICS, 10,
                    COMPUTER_SYSTEMS_ARCHITECTURE, 10,
                    OPERATING_SYSTEMS, 10,
                    LOCAL_AREA_NETWORKS, 10,
                    ASSEMBLY_LANGUAGE_PROGRAMMING, 10,
                    COMMUNICATION_PROTOCOLS, 10,
                    COMPUTERS_PROGRAMMING, 10)),
            new Student(
                "Constantin",
                "Raducanu",
                Map.of(
                    COMPILERS,10,
                    OBJECT_ORIENTED_PROGRAMMING, 10,
                    PARALLEL_AND_DISTRIBUTED_ALGORITHMS, 9,
                    COMPUTER_GRAPHICS, 10,
                    ALGORITHMS_ANALYSIS, 8,
                    ALGORITHMS_DESIGN, 9,
                    OPERATING_SYSTEMS, 6,
                    ASSEMBLY_LANGUAGE_PROGRAMMING, 5,
                    LOCAL_AREA_NETWORKS, 5,
                    COMPUTERS_PROGRAMMING, 10)),
            new Student(
                "Sebastian",
                "Oprea",
                Map.of(
                    OBJECT_ORIENTED_PROGRAMMING, 5,
                    PARALLEL_AND_DISTRIBUTED_ALGORITHMS, 6,
                    ALGORITHMS_ANALYSIS, 6,
                    ALGORITHMS_DESIGN, 6,
                    OPERATING_SYSTEMS, 6,
                    ASSEMBLY_LANGUAGE_PROGRAMMING, 5,
                    LOCAL_AREA_NETWORKS, 5,
                    COMPUTERS_PROGRAMMING, 6,
                    COMPUTER_SYSTEMS_ARCHITECTURE, 5,
                    LOGIC_DESIGN, 7))
        ));
    }
}

class Test {     							// Class for testing the functionality of the code for some of the above implemented
	@SuppressWarnings("static-access")		// methods
	public static void main(String[] args) {
		Helpers h = new Helpers();
		h.addStudents();
		h.addTeachers();
		Database db = Database.getInstance();
		System.out.println(db.findTeachersBySubject("Communication Protocols"));
		System.out.println();
		System.out.println(db.getStudentByGradeForSubject("Object Oriented Programming"));
		System.out.println();
		System.out.println("Number of instances for the Database object : " + db.getNumberOfInstances());
	}
}