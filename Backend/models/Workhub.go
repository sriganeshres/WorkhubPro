package models

import "gorm.io/gorm"

type WorkHub struct {
	gorm.Model
	Name        string     `gorm:"unique_index;not null" json:"name"`
	Admin       string     `gorm:"not null" json:"adminname"`
	Description string     `gorm:"not null" json:"description"`
	Domain      string     `gorm:"not null" json:"domain"`
	PrivacyKey  int        `gorm:"not null" json:"privacy_key"`
	Users       []UserData `gorm:"foreignKey:WorkhubID"`
}

type JoinWorkHub struct {
	UserEmail  string `json:"useremail"`
	PrivacyKey int    `json:"key"`
}
