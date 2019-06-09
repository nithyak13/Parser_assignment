package Assignment;

public class Employee {
	 private String accId;
     private int accNo;
     private String accType;
     private String bankName;
     private String firstName;
     private String lastName;
     private int balance;
 
     public Employee(String accId, int accNo,String accType,String bankName,String firstName,String lastname,int balance) {
          this.accId=accId;
          this.accNo = accNo;
          this.accType = accType;
          this.bankName = bankName;
          this.firstName = firstName;
          this.lastName=lastname;
          this.balance=balance;
     }
 
     @Override
     public String toString() {
          return "<" + accId + ", " + accNo + ", " +accType + ", " + bankName + ", "
                                   + firstName+" " +lastName+ "  " +balance+">";
     }

	public String getAccId() {
		return accId;
	}

	public void setAccId(String accId) {
		this.accId = accId;
	}

	public int getAccNo() {
		return accNo;
	}

	public void setAccNo(int accNo) {
		this.accNo = accNo;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}
}
