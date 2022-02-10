package com.company.assetmanagment.data.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter(AccessLevel.PUBLIC)
@Getter(AccessLevel.PUBLIC)
@Accessors(chain = true)
@Entity
@Table(name = "asset")
public class Asset
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column
	private Date createdOn;
	
	@Temporal(TemporalType.TIMESTAMP)
	@UpdateTimestamp
	@Column
	private Date updatedOn;
	
	@NotNull
	@Column(name = "name", 				length = 256, nullable = false)
	private String name;

	@Column(name = "type", 				length = 32,  nullable = false)
	private String type;
	
	@Column(name = "description", 		length = 1024, nullable = false)
	private String description;
}
