/*
 * Created by Calvin Huang, Zhengbo Wang, Lin Zhang on 2021.5.06
 * Copyright © 2021 Calvin Huang, Zhengbo Wang, Lin Zhang. All rights reserved.
 */
package edu.vt.EntityBeans;

import edu.vt.globals.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

// The @Entity annotation designates this class as a JPA Entity class representing the UserPhoto table in the CloudDriveDB database.
@Entity

// Name of the database table represented
@Table(name = "UserPhoto")

@NamedQueries({
    /* 
    The following query is added. The others are auto generated.  
    private User userId; --> userId is the object reference of the user.
    userId.id --> user object's database primary key
     */
    @NamedQuery(name = "UserPhoto.findPhotosByUserDatabasePrimaryKey", query = "SELECT p FROM UserPhoto p WHERE p.userId.id = :primaryKey")
    , @NamedQuery(name = "UserPhoto.findAll", query = "SELECT u FROM UserPhoto u")
    , @NamedQuery(name = "UserPhoto.findById", query = "SELECT u FROM UserPhoto u WHERE u.id = :id")
    , @NamedQuery(name = "UserPhoto.findByExtension", query = "SELECT u FROM UserPhoto u WHERE u.extension = :extension")
})

public class UserPhoto implements Serializable {

    /*
    ========================================================
    Instance variables representing the attributes (columns)
    of the UserPhoto table in the CloudDriveDB database.
    ========================================================
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "extension")
    private String extension;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    /*
    userId is the unique object reference of the User object auto generated
    by the system as an Integer value starting with 1 and incremented by 1,
    i.e., 1,2,3,... A deleted user's object reference number is not reused.
     */
    private User userId;

    /*
    =================================================================
    Class constructors for instantiating a UserPhoto entity object to
    represent a row in the UserPhoto table in the CloudDriveDB database.
    =================================================================
     */
    public UserPhoto() {
    }

    public UserPhoto(Integer id) {
        this.id = id;
    }

    public UserPhoto(Integer id, String extension) {
        this.id = id;
        this.extension = extension;
    }

    // This method is added to the generated code
    public UserPhoto(String fileExtension, User id) {
        this.extension = fileExtension;
        userId = id;
    }

    /*
    ======================================================
    Getter and Setter methods for the attributes (columns)
    of the UserPhoto table in the CloudDriveDB database.
    ======================================================
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    /*
    ================
    Instance Methods
    ================
     */
    /**
     * @return Generates and returns a hash code value for the object with id
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /**
     * Checks if the UserPhoto object identified by 'object' is the same as the UserPhoto object identified by 'id'
     *
     * @param object The UserPhoto object identified by 'object'
     * @return True if the UserPhoto 'object' and 'id' are the same; otherwise, return False
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserPhoto)) {
            return false;
        }
        UserPhoto other = (UserPhoto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        // Convert the UserPhoto object's database primary key (Integer) to String type and return it.
        return id.toString();
    }

    /*
    =====================================================
    The following methods are added to the generated code
    =====================================================
     
    User's photo image file is named as "userId.fileExtension", e.g., 5.jpg for user with id 5.
    Since the user can have only one photo, this makes sense.
     */
    public String getPhotoFilename() {
        return getUserId() + "." + getExtension();
    }

    /*
    --------------------------------------------------------------------------------
    The thumbnail photo image size is set to 200x200px in Constants.java as follows:
    public static final Integer THUMBNAIL_SIZE = 200;
    
    If the user uploads a large photo file, we will scale it down to THUMBNAIL_SIZE
    and use it so that the size is reasonable for performance reasons.
    
    The photo image scaling is properly done by using the imgscalr-lib-4.2.jar file.
    
    The thumbnail file is named as "userId_thumbnail.fileExtension", 
    e.g., 5_thumbnail.jpg for user with id 5.
    --------------------------------------------------------------------------------
     */
    public String getThumbnailFileName() {
        return getUserId() + "_thumbnail." + getExtension();
    }

    public String getPhotoFilePath() {
        return Constants.PHOTOS_ABSOLUTE_PATH + getPhotoFilename();
    }

    public String getThumbnailFilePath() {
        return Constants.PHOTOS_ABSOLUTE_PATH + getThumbnailFileName();
    }
}
