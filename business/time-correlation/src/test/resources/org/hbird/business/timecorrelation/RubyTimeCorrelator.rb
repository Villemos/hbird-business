require 'java'
require 'date'

include_class 'org.hbird.business.timecorrelation.TimeCorrelator'

class RubyTimeCorrelator
    include org.hbird.business.timecorrelation.TimeCorrelator
     
    def initialize()
      puts "RubyTimeCorrelator initialised"  
    end 
 
    def convertToSpacecraftTime(groundTime)
      puts "Converting " + groundTime.toString + " to spacecraft time"
      gTime = Time.at(groundTime.getTime/1000)
      puts gTime.to_i * 1000
    end
    
    def convertToGroundTime(spaceTime)
      puts "Converting " + spaceTime.to_s + " to spacecraft time"
      sTime = Time.at(spaceTime/1000)
      puts sTime.inspect
    end  
end


puts "Loading Ruby Time Correlator\n" 
$timeCorrelator = RubyTimeCorrelator.new() 