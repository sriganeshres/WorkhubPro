package models

import (
	"time"

	"gorm.io/gorm"
)

type Message struct {
	gorm.Model
	Message   string    `json:"msg"`
	Sender    string    `json:"sender"`
	Receiver  string    `json:"receiver"`
	CreatedAt time.Time `json:"CreatedAt"`
}
