# fastcsv
A reflection-less, column order agnostic CSV parser in Java

To use it, take your POJO and annotate to your heart’s content:

    public class Model {

      @Field("Field")
      private String field;
	
      @Field("Date")
      @FieldConverter(DateTimeConverter.class)
      private LocalDate date;
      
      public void setField(String field){
        this.field = field;
      }
      
      public void setDate(LocalDate date){
        this.date = date;
      }
    }

Note the ‘FieldConverter’, which is an implementation of the Converter interface:

    public class LocalDateConverter implements Converter<LocalDate>{
      @Override
      public LocalDate convert(String data) {
        return LocalDate.parse(data);
      }
    }

Then, using the CsvReader interface you can pass it an InputStream containing something that looks like CSV:

    CsvReader<Model> reader = CsvReaderImpl.getCsvReader(Model.class);
    ….
    reader.parse(stream);

The stream in this case, representing something like:

    Test,Field,Date
    1,abc,2010-10-05
    2,def,2010-10-06
    3,ghi,2010-10-07
    4,jkl,2010-10-08
    5,mno,2010-10-09
    6,pqr,2010-10-10

