.. _comparison:

Comparison with other packages
==============================

There exists other packages implementing similar methods in other languages, but with 
different goals in mind. To our knowledge, there are 2 software packages with the goal 
of learning from crowdsourced data:

* `Ceka <http://ceka.sourceforge.net/>`_: it is a Java software package based on WEKA, with 
  a great number of methods that can be used to learn from crowdsource data. 
* `Truth inference in Crowdsourcing <https://zhydhkcws.github.io/crowd_truth_inference/index.html/>`_ makes available a collection
  of methods in Python to learn from crowdsourced data. 

Both are useful packages when dealing with crowdsourced data, with a focus on research. `spark-crowd` is different, in the sense that
not only is useful in research, but in production as well, providing tests for all of its methods with a high test coverage. Moreover, 
methods have been implemented with a focus on scalability, so it is useful in a wide variety of situations. We provide a 
comparison of the methods over a set of datasets next, taking into account both quality of the models and execution time. 

Data
-----

For this performance test we use simulated datasets of increasing size:

* **binary1-4**: simulated binary class datasets with 10K, 100K, 1M and 10M instances respectively. Each of them 
  has 10 simulated annotations per instance, and the ground truth for each example is known (but not used in the 
  learning process). The accuracy shown in the tables is obtained over this known ground truth. 
* **cont1-4**: simulated continuous target variable datasets, with 10k, 100k, 1M and 10M instances respectively. Each of them
  has 10 simulated annotations per instance, and the ground truth for each example is known (but not used in the 
  learning process). The Mean Absolute Error is obtained over this known ground truth.  
* **crowdscale**. A real multiclass dataset from the *Crowdsourcing at Scale* challenge. The data is comprised of 98979 instances, 
  evaluated by, at least, 5 annotators, for a total of 569375 answers. We only have ground truth for the 0.3% of the data, 
  which is used for evaluation. 

All datasets are available through this `link <https://www.dropbox.com/sh/odmhdf83latvezu/AAB6om3Oy7-waf-msIvk9yX6a?dl=0>`_



CEKA
------

To compare our methods with Ceka, we used two of the main methods implemented in both packages, MajorityVoting and DawidSkene. Ceka and 
spark-crowd also implement GLAD and Raykar's algorithms. However, in Ceka, these algorithms are implemented using wrappers to other libraries. 
The library for the GLAD algorithm is not available on our platform, as it is given as an EXE Windows file, and the wrapper for Raykar's algorithms 
does not admit any configuration parameters. 

We provide the results of the execution of these methods in terms of accuracy (Acc) and time (in seconds). For our package, we also include 
the execution time for a cluster (tc) with 3 executor nodes of 10 cores and 30Gb of memory each. 

.. table:: Comparison with Ceka 

  +------------------------+-------------------------------------------------+---------------------------------------+
  |                        |   MajorityVoting                                | DawidSkene                            | 
  +------------------------+-------------------------+-----------------------+---------------+-----------------------+
  |                        |   Ceka                  | spark-crowd           | Ceka          | spark-crowd           |
  +------------------------+---------+---------------+---------+------+------+-------+-------+---------+------+------+
  | Method                 |   Acc   |     t1        |   Acc   |  t1  |  tc  | Acc   | t1    |   Acc   |  t1  |  tc  |
  +========================+=========+===============+=========+======+======+=======+=======+=========+======+======+
  | binary1                | 0.931   |     21        | 0.931   |  11  |   7  | 0.994 | 57    |  0.994  |  31  |  32  |
  +------------------------+---------+---------------+---------+------+------+-------+-------+---------+------+------+
  | binary2                | 0.936   |  15983        | 0.936   |  11  |   7  | 0.994 | 49259 |  0.994  |  60  |  51  |
  +------------------------+---------+---------------+---------+------+------+-------+-------+---------+------+------+
  | binary3                |   X     |     X         | 0.936   |  21  |   8  | X     | X     |  0.994  | 111  |  69  |
  +------------------------+---------+---------------+---------+------+------+-------+-------+---------+------+------+
  | binary4                |   X     |     X         | 0.936   |  84  |  42  | X     | X     | 0.994   |      |      |
  +------------------------+---------+---------------+---------+------+------+-------+-------+---------+------+------+
  | crowdscale             |  0.88   |   10458       | 0.9     |  13  |  7   | 0.89  | 30999 | 0.9033  | 447  |  86  |
  +------------------------+---------+---------------+---------+------+------+-------+-------+---------+------+------+
 
Regarding accuracy, both packages achieve comparable results. However, regarding execution time, spark-crowd obtains 
significantly better results among all datasets especially on the bigger datasets, where it can solve problems that 
Ceka is not able to. You can see the speedup results in the table below.

.. table:: Speedup in comparison to Ceka 

  +------------------------+-------------------------------------+
  |                        |  MajorityVoting  |      DawidSkene  | 
  +------------------------+--------+---------+--------+---------+
  | Method                 |  t1    |  tc     |   t1   |     tc  |
  +========================+========+=========+========+=========+
  | binary1                | 1.86   |  2.93   |  1.84  |   1.78  |
  +------------------------+--------+---------+--------+---------+
  | binary2                |  1453  |  2283   |  272   |  1146   |
  +------------------------+--------+---------+--------+---------+
  | crowdscale             |  804   |  1494   |  69    |  360    |
  +------------------------+--------+---------+--------+---------+


We can see that spark-crowd obtains a high speedup in bigger datasets and performs 
slightly better in the smaller ones. 


Truth inference in crowdsourcing
----------------------------------

Now we compare spark-crowd with the methods available in this paper. Although the methods 
can certainly be used for to compare and try the algorithms, the integration of these 
methods into a large ecosystem will be very difficult, as the authors do not provide 
a software package structure. However, as it is an available package with a great number 
of methods, a comparison with them is needed. We will use the same datasets 
as the ones used in the previous comparison. In this case, we can compare a higher
number of models, as most of the methods are written in python. However, we were only able 
to execute the methods over datasets with binary or continuous target variables. As far as we 
know, the use of multiclass target variables seems to not be possible. Moreover, the use of 
feature sets is also restricted, although algorithms that should be capable of dealing with 
this kind of data are implemented, as is the case with the Raykar's methods. 

First, we compare the algorithms capable of learning from binary classes without feature sets. 
Inside this category, we will compare MajorityVoting, DawidSkene, GLAD and IBCC. For each dataset, we show 
results in terms of Accuracy (Acc) and time (in seconds). The table below shows the results for 
MajorityVoting and DawidSkene. Both packages obtain the same results in terms of 
accuracy. For the smaller datasets, the overhead imposed by parallelism makes Truth-inf a better choice, 
at least in terms of execution time. However, as the datasets increase, and specially, in the last two 
cases, the speedup obtained by our algorithm is notable. In the case of DawidSkene, the Truth-inf 
package is not able to complete the execution because of memory constraints in the largest dataset.  


.. table:: Comparative with Truth inference in Crowdsourcing package 

  +------------------------+-------------------------------------------------+---------------------------------------+
  |                        |   MajorityVoting                                | DawidSkene                            | 
  +------------------------+-------------------------+-----------------------+---------------+-----------------------+
  |                        |   Truth-inf             | spark-crowd           | Truth-inf     | spark-crowd           |
  +------------------------+---------+---------------+---------+------+------+-------+-------+---------+------+------+
  | Method                 |   Acc   |     t1        |   Acc   |  t1  |  tc  | Acc   | t1    |   Acc   |  t1  |  tc  |
  +========================+=========+===============+=========+======+======+=======+=======+=========+======+======+
  | binary1                | 0.931   |   1           | 0.931   |  11  |   7  | 0.994 | 12    |  0.994  | 31   | 32   |
  +------------------------+---------+---------------+---------+------+------+-------+-------+---------+------+------+
  | binary2                | 0.936   |   8           | 0.936   |  11  |   7  | 0.994 | 161   |  0.994  | 60   | 51   |
  +------------------------+---------+---------------+---------+------+------+-------+-------+---------+------+------+
  | binary3                | 0.936   |   112         | 0.936   |  21  |   8  | 0.994 | 1705  |  0.994  | 111  | 69   |
  +------------------------+---------+---------------+---------+------+------+-------+-------+---------+------+------+
  | binary4                |  0.936  |   2908        | 0.936   |  13  |  7   |   M   |   M   |  0.994  | 703  | 426  |
  +------------------------+---------+---------------+---------+------+------+-------+-------+---------+------+------+


Next we show the results for GLAD and IBCC. As the user can see, both packages obtain similar results 
in terms of accuracy. Regarding execution time, both packages obtain similar results 
in the two smaller datasets (with a slight speedup in the ``binary2``) for the GLAD algorithm. However, 
Truth-inf is not able to complete the execution for the two largest datasets. A last thing to notice 
regarding the last execution of this algorithm is that at large scale, the performance of the 
algorithm seems to degrade. To tackle this kind of problems, we developed and enhancement, CGlad, recently
published and which is included in the package (See the last section of this page for results of other 
methods in the package, as well as this enhancement)
In the case of IBCC, the speedup starts to be noticeable from the second dataset. Again, Truth-inf was 
unable to complete the execution in a reasonable ammount of time for the last dataset.  





.. table:: Comparative with Truth inference in Crowdsourcing package (2)

  +------------------------+---------------------------------------+---------------------------------------+
  |                        | GLAD                                  | IBCC                                  |
  +------------------------+---------------+-----------------------+---------------+-----------------------+
  |                        | Truth-inf     | spark-crowd           | Truth-inf     | spark-crowd           |
  +------------------------+-------+-------+---------+------+------+-------+-------+---------+------+------+
  | Method                 | Acc   | t1    |   Acc   |  t1  |  tc  | Acc   | t1    |   Acc   |  t1  |  tc  |
  +========================+=======+=======+=========+======+======+=======+=======+=========+======+======+
  | binary1                | 0.994 | 1185  |  0.994  | 1568 | 1547 | 0.994 | 22    |  0.994  | 74   | 67   |
  +------------------------+-------+-------+---------+------+------+-------+-------+---------+------+------+
  | binary2                | 0.994 | 4168  |  0.994  | 2959 | 2051 | 0.994 | 372   |  0.994  | 97   | 76   |
  +------------------------+-------+-------+---------+------+------+-------+-------+---------+------+------+
  | binary3                | X     | X     |  0.994  | 7538 | 4770 | 0.994 | 25764 |  0.994  | 203  | 129  |
  +------------------------+-------+-------+---------+------+------+-------+-------+---------+------+------+
  | binary4                | X     | X     |  0.974  | 2407 | 1158 |   X   |   X   |    X    | 1529 | 823  |
  +------------------------+-------+-------+---------+------+------+-------+-------+---------+------+------+





Next we analize methods that are able to learn from continuous target variables: MajorityVoting (mean), CATD and PM (with mean initialization). We show the results in terms of MAE (Mean absolute error) and time (in seconds). The 
results for MajorityVoting and CATD can be found below in the table below. 


.. table:: Comparative with Truth inference in Crowdsourcing package on continuous target variables 

  +------------------------+-------------------------------------------------+---------------------------------------+
  |                        |   MajorityVoting (mean)                         | CATD                                  | 
  +------------------------+-------------------------+-----------------------+---------------+-----------------------+
  |                        |   Truth-inf             | spark-crowd           | Truth-inf     | spark-crowd           |
  +------------------------+---------+---------------+---------+------+------+-------+-------+---------+------+------+
  | Method                 |   Acc   |     t1        |   Acc   |  t1  |  tc  | Acc   | t1    |   Acc   |  t1  |  tc  |
  +========================+=========+===============+=========+======+======+=======+=======+=========+======+======+
  | cont1                  | 1.234   |      1        | 1.234   |  6   |  8   | 0.324 | 207   |  0.324  | 25   | 28   |
  +------------------------+---------+---------------+---------+------+------+-------+-------+---------+------+------+
  | cont2                  | 1.231   |      8        | 1.231   | 7    |  9   | 0.321 | 10429 |  0.321  | 26   | 24   |
  +------------------------+---------+---------------+---------+------+------+-------+-------+---------+------+------+
  | cont3                  | 1.231   |     74        | 1.231   | 12   | 13   |   X   |   X   |  0.322  | 42   | 38   |
  +------------------------+---------+---------------+---------+------+------+-------+-------+---------+------+------+
  | cont4                  | 1.231   |    581        | 1.231   | 56   | 23   |   X   |   X   |  0.322  | 247  | 176  |
  +------------------------+---------+---------------+---------+------+------+-------+-------+---------+------+------+


As you can see in the table, both packages obtain similar results regarding MAE. Regarding performance, 
MajorityVoting is quite performant in the Truth-inf package, specially in the smaller dataset. For smaller datasets,
the increase overhead impose by parallelism makes the execution time of our package a little worse in comparison. 
However, as the dataset increase in size, the speedup obtained by our package is notable, even in this algorithm, 
which is less complex computationally. Regarding CATD, Truth-inf seems not to be able to solve the 2 bigger problems 
in a reasonable time, however, they can be solved by our package in a small ammount of time. Even for the smaller 
datasets, our package obtains a high speedup in comparison to Truth-inf.

In the table below you can find the results for PM and PMTI algorithms. 



.. table:: Comparative with Truth inference in Crowdsourcing package on continuous target variables (2)

   +------------------------+---------------------------------------+---------------------------------------+
   |                        | PM                                    | PMTI                                  |
   +------------------------+---------------+-----------------------+---------------+-----------------------+
   |                        | Truth-inf     | spark-crowd           | Truth-inf     | spark-crowd           |
   +------------------------+-------+-------+---------+------+------+-------+-------+---------+------+------+
   | Method                 | Acc   | t1    |   Acc   |  t1  |  tc  | Acc   | t1    |   Acc   |  t1  |  tc  |
   +========================+=======+=======+=========+======+======+=======+=======+=========+======+======+
   | cont1                  | 0.495 | 77    |  0.495  | 57   | 51   | 0.388 | 139   |  0.388  | 68   |  61  |
   +------------------------+-------+-------+---------+------+------+-------+-------+---------+------+------+
   | cont2                  | 0.493 | 8079  |  0.495  | 76   | 57   | 0.386 | 14167 |  0.386  | 74   |  58  |
   +------------------------+-------+-------+---------+------+------+-------+-------+---------+------+------+
   | cont3                  | X     |  X    |  0.494  | 130  | 97   | X     |  X    |  0.387  | 143  |  98  |
   +------------------------+-------+-------+---------+------+------+-------+-------+---------+------+------+
   | cont4                  | X     |  X    |  0.494  | 769  | 421  | X     |  X    |  0.387  | 996  | 475  |
   +------------------------+-------+-------+---------+------+------+-------+-------+---------+------+------+

Although similar, the modification implemented in Truth-inf from the original algorithm seems to be more 
accurate. The code for the original version was also available, although it was commented in the source code. 
Even in the smaller dataset, our package obtains a slight speedup. However as the datasets increase in size, 
our package is able to obtain a much higher speedup. As was the case with CATD, it was impossible for us to 
solve them in a reasonable ammount of time with Truth-inf. 

Other methods
---------------

Experimentation will not be complete without looking at the other methods implemented by our package that 
are not directly implemented by the packages above. These methods are the full implementation of the Raykar's 
algorithms (taking into account the features of the instances) and the enhancement over the GLAD algorithm. As a 
note, Truth-inf implements a version of Raykar's algorithms that do not use the features of the instances. First, 
we show the results obtained by the Raykar's methods for discrete target variables. 


.. table:: Other methods implemented in spark-crowd. Raykar's methods for discrete target variables. 

   +------------------------+---------------------------------------+---------------------------------------+
   |                        | RaykarBinary                          | RaykarMulti                           |
   +------------------------+---------------------------------------+---------------------------------------+
   |                        | spark-crowd                           | spark-crowd                           |
   +------------------------+---------+------+----------------------+---------+------+----------------------+
   | Method                 |   Acc   |  t1  |  tc                  |   Acc   |  t1  |  tc                  |
   +========================+=========+======+======================+=========+======+======================+
   | binary1                |  0.994  | 31   | 32                   |  0.994  | 31   | 32                   |
   +------------------------+---------+------+----------------------+---------+------+----------------------+
   | binary2                |  0.994  | 60   | 51                   |  0.994  | 60   | 51                   |
   +------------------------+---------+------+----------------------+---------+------+----------------------+
   | binary3                |  0.994  | 111  | 69                   |  0.994  | 111  | 69                   |
   +------------------------+---------+------+----------------------+---------+------+----------------------+
   | binary4                |  0.994  | 703  | 426                  |  0.994  | 703  | 426                  |
   +------------------------+---------+------+----------------------+---------+------+----------------------+


Next we show the Raykar method for tackling continous target variables. 


.. table:: Other methods implemented in spark-crowd. Raykar method for continuous target variables. 

   +------------------------+---------------------------------------+
   |                        | RaykarCont                            |
   +------------------------+---------------+-----------------------+
   |                        | spark-crowd                           |
   +------------------------+---------+------+----------------------+
   | Method                 |   Acc   |  t1  |  tc                  |
   +========================+=========+======+======================+
   | cont1                  |  0.994  | 31   | 32                   |
   +------------------------+---------+------+----------------------+
   | cont2                  |  0.994  | 60   | 51                   |
   +------------------------+---------+------+----------------------+
   | cont3                  |  0.994  | 111  | 69                   |
   +------------------------+---------+------+----------------------+
   | cont4                  |  0.994  | 703  | 426                  |
   +------------------------+---------+------+----------------------+

Lastly, we show the results for the CGlad algorithm. As you can see, it obtains similar results to the GLAD algorithm
but it performs better in the largest case, which makes it a good alternative to GLAD in such cases. 

.. table:: Other methods implemented in spark-crowd. CGlad, an enhancement over Glad algorithm. 

   +------------------------+---------------------------------------+
   |                        | CGlad                                 |
   +------------------------+---------------+-----------------------+
   |                        | spark-crowd                           |
   +------------------------+---------+------+----------------------+
   | Method                 |   Acc   |  t1  |  tc                  |
   +========================+=========+======+======================+
   | binary1                |  0.994  | 31   | 32                   |
   +------------------------+---------+------+----------------------+
   | binary2                |  0.994  | 60   | 51                   |
   +------------------------+---------+------+----------------------+
   | binary3                |  0.994  | 111  | 69                   |
   +------------------------+---------+------+----------------------+
   | binary4                |  0.994  | 703  | 426                  |
   +------------------------+---------+------+----------------------+