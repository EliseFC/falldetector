df <- read.csv("acc.csv", col.names = c("time", "ax", "ay", "az"), as.is = c(1))
df$aa <- df$ax^2 + df$ay^2 + df$az^2
df$time <- strptime(df$time, "%a %b %e %H:%M:%S EDT %Y")
df$rowid <- as.integer(row.names(df))
# plot(df$time, df$aa, type = "l")
# abline(h = 35, col = "red")
falls <- subset(df, df$aa < 50)

# plot(falls$rowid, falls$aa, type = "l")
# abline(h = 35, col = "red")

draw_graph <- function(df) {
        dev.off()
        with(df, plot(rowid, aa, type = "l"))
        with(df, points(rowid, aa))
        abline(h = 35, col = "green")
        with(subset(df, df$aa < 35), points(rowid, aa, col = "red"))
}

s <- subset(df, df$time == as.POSIXlt("2017-03-25 22:22:59"))
draw_graph(s)
