/*
 * Created by Calvin Huang on 2021.3.24
 * Copyright © 2021 Calvin Huang. All rights reserved.
 */
package edu.vt.EntityBeans;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

// The @Entity annotation designates this class as a JPA Entity class representing the User table in the CloudDriveDB database.
@Entity

// Name of the database table represented
@Table(name = "Taker")

@NamedQueries({
    @NamedQuery(name = "Taker.findAll", query = "SELECT u FROM Taker u")
    , @NamedQuery(name = "Taker.findById", query = "SELECT u FROM Taker u WHERE u.id = :taker_id")
    , @NamedQuery(name = "Taker.findByLastName", query = "SELECT u FROM Taker u WHERE u.lastName = :last_name")
    , @NamedQuery(name = "Taker.findByFirstName", query = "SELECT u FROM Taker u WHERE u.firstName = :first_name")})


public class Taker implements Serializable {
    /*
    ========================================================
    Instance variables representing the attributes (columns)
    of the User table in the CloudDriveDB database.
    ========================================================
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "taker_id")
    private Integer id;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "first_name")
    private String firstName;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "last_name")
    private String lastName;
    /*
    ===============================================================
    Class constructors for instantiating a User entity object to
    represent a row in the User table in the CloudDriveDB database.
    ===============================================================
     */
    public Taker() {
    }

    public Taker(Integer id) {
        this.id = id;
    }

    public Taker(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }



     /**
     * Checks if the User object identified by 'object' is the same as the User object identified by 'id'
     *
     * @param object The User object identified by 'object'
     * @return True if the User 'object' and 'id' are the same; otherwise, return False
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Taker)) {
            return false;
        }
        Taker other = (Taker) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        // Convert the User object's database primary key (Integer) to String type and return it.
        return id.toString();
    }

}
