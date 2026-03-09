package com.capg.assessment.entity;
import javax.persistence.*;

@Entity
@Table(name = "lead")
public class Lead {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lead_seq")
	@SequenceGenerator(name = "lead_seq", sequenceName = "lead_seq", allocationSize = 1)
	private Long leadId;
	
	private String name;
	private String source;
	private String contactInfo;
	
	@ManyToOne
	@JoinColumn(name = "employee_id")
	private SalesEmployee employee;
	
	public Lead() {}

	public Long getLeadId() {
		return leadId;
	}

	public void setLeadId(Long leadId) {
		this.leadId = leadId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public SalesEmployee getEmployee() {
		return employee;
	}

	public void setEmployee(SalesEmployee employee) {
		this.employee = employee;
	}  
}
