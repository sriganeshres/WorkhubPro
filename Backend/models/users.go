package models

import "github.com/jinzhu/gorm"

type RoleField string

const (
	User        RoleField = "user"
	Admin       RoleField = "admin"
	ProjectLead RoleField = "project_lead"
)

type UserData struct {
	gorm.Model
	Username  string    `gorm:"unique_index;not null" json:"username"`
	Password  string    `json:"password"`
	Email     string    `gorm:"unique_index;not null" json:"email"`
	Role      RoleField `json:"role" gorm:"default:'user'"`
	WorkhubID uint      `json:"id" gorm:"foreignKey:WorkhubID"`
	ProjectID *uint     `gorm:"foreignKey:ProjectID;null" json:"project_id"`
}

type LoginUser struct {
	Email    string `json:"email"`
	Password string `json:"password"`
}

type AddUsersToProject struct {
	Names     []string `json:"names"`
	ProjectID int      `json:"project_id"`
	WorkHubID int      `json:"workhub_id"`
}
