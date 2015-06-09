library(ggplot2)

raw <- read.csv('data.csv', sep=',', header=FALSE)
my.data <- data.frame(t(raw)[-1,])
names(my.data) <- t(raw)[1,]
my.data$size <- 1:nrow(my.data)
reshaped <- reshape(my.data, direction='long', varying=names(my.data)[1:3], v.names="collisions", idvar="size", timevar="implementation", times=names(my.data)[1:3])
reshaped$implementation <- as.factor(reshaped$implementation)
reshaped$collisions <- as.numeric(as.character(reshaped$collisions))
ggplot(reshaped, aes(x=size, y=collisions, group=implementation, color=implementation)) + geom_line() + ggtitle('Cumulative Collisions for Hash Set Implementations')
