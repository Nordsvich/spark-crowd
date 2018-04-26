import com.enriquegrodrigo.spark.crowd.methods.IBCC
import com.enriquegrodrigo.spark.crowd.types._

sc.setCheckpointDir("checkpoint")

val annFile = "examples/data/multi-ann.parquet"

val annData = spark.read.parquet(annFile)

//Applying the learning algorithm
val mode = IBCC(annData)

//Get MulticlassLabel with the class predictions
val pred = mode.mu

//Annotator precision matrices
val annprec = mode.pi

//Annotator precision matrices
val classPrior = mode.p

