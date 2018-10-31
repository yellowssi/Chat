#include "src/ui/mainwindow.h"
#include "ui_mainwindow.h"
#include "mainwindow.h"


MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
}

MainWindow::~MainWindow()
{
    tcpServer->close();
    delete ui;
}

void MainWindow::sendMessage() {
    QByteArray byteArray;
    QDataStream out(&byteArray, QIODevice::WriteOnly);
}
