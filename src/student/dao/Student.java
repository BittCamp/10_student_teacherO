package student.dao;//[ 김찬영  2023-07-28 오전 11:16:19 ]

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Student {
	private Connection conn;
	private PreparedStatement  pstmt;
	Scanner scan = new Scanner(System.in);
	private ResultSet rs;
	String sql;
	
	//자르는 과정. 환경변수로 빼버리는 과정을 할거라
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url ="jdbc:oracle:thin:@localhost:1521:xe";
	private String username ="c##java";
	private String password = "1234";
	private int num;

	public Student() {	// 생성자 . 딱 1번밖에 수행안함.
		try {
			Class.forName(driver);
			System.out.println("드라이버 로딩 성공");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} // 앞에 패키지명까지 완벽하게 써라.
	}
	public void menu() {
		while(true) {
			System.out.println();
			System.out.println("****************");
			System.out.println(" 1. 입력 \n 2. 검색 \n 3. 삭제 \n 4. 종료");
			System.out.println("****************");
			System.out.print(" 번호 선택 ");
			System.out.println();
			num = scan.nextInt();
			if(num == 4) break; 
			if(num == 1) insertArticle();
			if(num == 2) selectArticle();
			if(num == 3) deleteArticle();
			
			
		}//while
	}//menu()
	public void getConnection() {
		try { //오라클은 1521 // 지포스 엔비디아 그래픽카드 드라이버명. 오라클은 thin으로 쓴다.
			conn = DriverManager.getConnection(url,username,password);
			//mysql 밑에꺼 Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/boarddb", "root", "12341234!");
			//System.out.println("접속 성공");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void insertArticle() {
		while(true) {
		System.out.println();			
		System.out.println("****************");
		System.out.println(" 1. 학생 \n 2. 교수 \n 3. 관리자 \n 4. 이전메뉴");
		System.out.print("****************");
		System.out.println();
		int code = scan.nextInt();
		if(code == 4) break;
		System.out.println("이름을 입력하세요.");
		String name = scan.next(); 
		if(code == 1) System.out.println("학번입력");
		if(code == 2) System.out.println("과목입력");
		if(code == 3) System.out.println("부서입력");
		String value = scan.next(); 
		String sql = "insert into student2 values (?,?,?)";
		try {
			this.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,name);
			pstmt.setString(2,value);
			pstmt.setInt(3,code);
			int su = pstmt.executeUpdate(); // 개수가 리턴됨.
			System.out.println(su + "개의 행이 입력 되었습니다.");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close(); // 열어놓은거 안닫으면 메모리에 계속쌓인다. 무조건 닫아야.
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		}
	}
	
	public void selectArticle() {
		while(true) {
			System.out.println();			
			System.out.println("****************");
			System.out.println(" 1. 이름 검색 \n 2. 전체 검색 \n 3. 이전메뉴");
			System.out.print("****************");
			System.out.println();
			num = scan.nextInt();
			if(num == 3) break;
			this.getConnection();
			if(num == 1) {System.out.println("이름을 입력하세요"); sql = "select name,value,code from student2 where name like ?";} 
			else 		 {sql = "select name,value,code from student2";}
				try {
					pstmt = conn.prepareStatement(sql);
					if( num ==1 ) {
						String name = scan.next();
						pstmt.setString(1,"%"+name+"%");
					} 
					int su = pstmt.executeUpdate(); 
					System.out.println(su+"건 이 검색되었습니다.");
					
					rs = pstmt.executeQuery();
					while(rs.next()) {
						System.out.print(rs.getString("name")+"\t");
						if(rs.getInt("code")==1) System.out.println("학번 = "+ rs.getString("value"));
						else if(rs.getInt("code")==2) System.out.println("과목 = "+ rs.getString("value"));
						else if(rs.getInt("code")==3) System.out.println("부서 = "+ rs.getString("value"));
					}//while
				} catch (SQLException e) {
					e.printStackTrace();
				} finally { // 에러가있든 없든 무조건 수행해라.
					try {
						if(pstmt != null) pstmt.close(); // 열어놓은거 안닫으면 메모리에 계속쌓인다. 무조건 닫아야.
						if(conn != null) conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}	
			}
	}
	public void deleteArticle() {
		while(true) {
		System.out.println();			
		System.out.println("****************");
		System.out.println(" 1.삭제 \n 2. 이전메뉴");
		System.out.print("****************");
		System.out.println();
		int code = scan.nextInt();
		if(code == 2) break;
		System.out.println("삭제할 이름을 입력하세요.");
		String name = scan.next(); 
		String sql = "delete student2 where name = ?";// delete student2 where name = ? 도 되고 from 써도 된다.
		try {
			this.getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,name);
			int su = pstmt.executeUpdate(); // 개수가 리턴됨.
			System.out.println(su + "개의 행이 삭제 되었습니다.");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null) pstmt.close(); // 열어놓은거 안닫으면 메모리에 계속쌓인다. 무조건 닫아야.
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
		}
	}
	public static void main(String[] args) {
		Student student = new Student();
		student.menu();
		System.out.println("프로그램을 종료합니다.");
	}
}
