package models

import (
	"github.com/jinzhu/gorm"
)

type StatusField string

const (
	InProgress StatusField = "inProgress"
	Completed  StatusField = "completed"
	NotStarted StatusField = "notStarted"
)

type Task struct {
	gorm.Model
	Name        string      `gorm:"unique_index;not null" json:"name"`
	Description string      `json:"description"`
	AssignedBy  int      	`json:"assigned_by" gorm:"foreignKey:UserID"`
	AssignedTo  int         `json:"assigned_to" gorm:"foreignKey:UserID"`
	ProjectID   int         `gorm:"foreignKey:ProjectID" json:"project_key"`
	WorkHubID   int         `gorm:"foreignKey:WorkHubID" json:"work_hub_id"`
	Status      StatusField `gorm:"default:'notStarted'" json:"status"`
}

type UpdateTask struct {
	ID          int         ` json:"taskID"`
	StatusField StatusField `json:"status"`
}
