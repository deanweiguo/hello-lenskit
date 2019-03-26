import org.grouplens.lenskit.transform.normalize.BaselineSubtractingUserVectorNormalizer
import org.grouplens.lenskit.transform.normalize.UserVectorNormalizer

import org.lenskit.baseline.BaselineScorer
import org.lenskit.baseline.ItemMeanRatingItemScorer
import org.lenskit.baseline.UserMeanBaseline
import org.lenskit.baseline.UserMeanItemScorer
import org.lenskit.knn.MinNeighbors


import org.lenskit.knn.user.UserUserItemScorer
import org.lenskit.knn.user.UserVectorSimilarity

bind ItemScorer to UserUserItemScorer.class

bind (BaselineScorer, ItemScorer) to UserMeanItemScorer

bind (UserMeanBaseline, ItemScorer) to ItemMeanRatingItemScorer


set MinNeighbors to 20
