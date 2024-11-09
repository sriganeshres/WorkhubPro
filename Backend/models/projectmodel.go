package models

import "gorm.io/gorm"

type Project struct {
	gorm.Model
	Name        string     `gorm:"unique_index;not null" json:"name"`
	Description string     `json:"description"`
	Members     []UserData `gorm:"foreignKey:ProjectID"`
	ProjectLead string     `json:"project_leader"`
	ProjectID   int        `gorm:"not null" json:"project_key"`
	WorkhubID   int        `gorm:"not null" json:"workhub_id"`
}

type ProjectCreationRequest struct {
	Name        string   `json:"name"`
	Description string   `json:"description"`
	ProjectLead string   `json:"project_leader"`
	WorkhubID   int      `json:"workhub_id"`
	Members     []string `json:"members"`
}
