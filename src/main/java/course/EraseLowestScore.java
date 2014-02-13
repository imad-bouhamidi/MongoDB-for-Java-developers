package course;


import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.List;


public class EraseLowestScore {


    public static void main(String[] args) throws UnknownHostException{
        MongoClient mongoClient = new MongoClient();
        DB db = mongoClient.getDB("school");
        DBCollection StudentsCollection = db.getCollection("students");

        DBCursor cursor = StudentsCollection.find();

        while (cursor.hasNext()){
            DBObject NextStudent = cursor.next();
            List<DBObject> scores = (List<DBObject>) NextStudent.get("scores");
            Integer id = (Integer) NextStudent.get("_id");
            int i;
            Double scorMin = 1000.0;
            for (i = 0; i < scores.size(); i++) {
                DBObject score = scores.get(i);
                String type = (String) score.get("type");
                if (type.equals("homework")) {
                    Double curentScore = (Double) score.get("score");
                    if(curentScore < scorMin){
                       scorMin = curentScore;
                    }


                }
            }

            DBObject lowestScore = new BasicDBObject("type", "homework").append("score", scorMin);
            scores.remove(lowestScore);

            StudentsCollection.update(new BasicDBObject("_id", id),new BasicDBObject("$set",new BasicDBObject("scores", scores)));
        }



    }
}
