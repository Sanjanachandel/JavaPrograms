package com.capg.assessment.entity;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "sales_employee")
public class SalesEmployee {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq")
	@SequenceGenerator(name = "employee_seq", sequenceName = "employee_seq", allocationSize = 1)
	private Long empId;
    private String name;
    private String department;
    
    @OneToMany(mappedBy = "employee")
    private List<Lead> leads;
    
    public SalesEmployee() {}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public List<Lead> getLeads() {
		return leads;
	}

	public void setLeads(List<Lead> leads) {
		this.leads = leads;
	}
    
    
    
} 
