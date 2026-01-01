package com.student;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

public class App {

    static SessionFactory factory;

    public static void main(String[] args) {

        factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== STUDENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Student");
            System.out.println("2. View Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Search Student by ID");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addStudent(sc);
                    break;
                case 2:
                    viewStudents();
                    break;
                case 3:
                    updateStudent(sc);
                    break;
                case 4:
                    deleteStudent(sc);
                    break;
                case 5:
                    searchStudentById(sc);
                    break;
                case 6:
                    factory.close();
                    sc.close();
                    System.out.println("Thank you! Program exited.");
                    return;
                default:
                    System.out.println("❌ Invalid choice!");
            }
        }
    }

    // ---------------- ADD STUDENT ----------------
    static void addStudent(Scanner sc) {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            System.out.print("Enter name: ");
            String name = sc.nextLine();

            System.out.print("Enter age: ");
            int age = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter course: ");
            String course = sc.nextLine();

            // ✅ Input Validation
            if (name.isEmpty() || course.isEmpty() || age <= 0) {
                System.out.println("❌ Invalid input. Please try again.");
                return;
            }

            tx = session.beginTransaction();
            Student s = new Student(name, age, course);
            session.save(s);
            tx.commit();

            System.out.println("✅ Student added successfully!");

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("❌ Error while adding student.");
        } finally {
            session.close();
        }
    }

    // ---------------- VIEW STUDENTS ----------------
    static void viewStudents() {
        Session session = factory.openSession();

        try {
            Query<Student> query = session.createQuery("from Student", Student.class);
            List<Student> list = query.list();

            System.out.println("\n--- Student List ---");
            for (Student s : list) {
                System.out.println(
                        s.getId() + " | " +
                        s.getName() + " | " +
                        s.getAge() + " | " +
                        s.getCourse()
                );
            }
        } catch (Exception e) {
            System.out.println("❌ Error fetching students.");
        } finally {
            session.close();
        }
    }

    // ---------------- UPDATE STUDENT ----------------
    static void updateStudent(Scanner sc) {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            System.out.print("Enter student ID to update: ");
            int id = sc.nextInt();
            sc.nextLine();

            Student s = session.get(Student.class, id);

            if (s == null) {
                System.out.println("❌ Student not found!");
                return;
            }

            System.out.print("Enter new name: ");
            s.setName(sc.nextLine());

            System.out.print("Enter new age: ");
            s.setAge(sc.nextInt());
            sc.nextLine();

            System.out.print("Enter new course: ");
            s.setCourse(sc.nextLine());

            tx = session.beginTransaction();
            session.update(s);
            tx.commit();

            System.out.println("✅ Student updated successfully!");

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("❌ Error updating student.");
        } finally {
            session.close();
        }
    }

    // ---------------- DELETE STUDENT ----------------
    static void deleteStudent(Scanner sc) {
        Session session = factory.openSession();
        Transaction tx = null;

        try {
            System.out.print("Enter student ID to delete: ");
            int id = sc.nextInt();

            Student s = session.get(Student.class, id);

            if (s == null) {
                System.out.println("❌ Student not found!");
                return;
            }

            System.out.print("Are you sure? (yes/no): ");
            sc.nextLine();
            String confirm = sc.nextLine();

            if (!confirm.equalsIgnoreCase("yes")) {
                System.out.println("❌ Delete cancelled.");
                return;
            }

            tx = session.beginTransaction();
            session.delete(s);
            tx.commit();

            System.out.println("✅ Student deleted successfully!");

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("❌ Error deleting student.");
        } finally {
            session.close();
        }
    }

    // ---------------- SEARCH STUDENT BY ID ----------------
    static void searchStudentById(Scanner sc) {
        Session session = factory.openSession();

        try {
            System.out.print("Enter student ID to search: ");
            int id = sc.nextInt();

            Student s = session.get(Student.class, id);

            if (s != null) {
                System.out.println("\n--- Student Found ---");
                System.out.println(
                        s.getId() + " | " +
                        s.getName() + " | " +
                        s.getAge() + " | " +
                        s.getCourse()
                );
            } else {
                System.out.println("❌ Student not found!");
            }

        } catch (Exception e) {
            System.out.println("❌ Error searching student.");
        } finally {
            session.close();
        }
    }
}
