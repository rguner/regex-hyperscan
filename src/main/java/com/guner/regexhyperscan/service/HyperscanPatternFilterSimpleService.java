package com.guner.regexhyperscan.service;

import com.gliwka.hyperscan.util.PatternFilter;
import com.gliwka.hyperscan.wrapper.CompileErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class HyperscanPatternFilterSimpleService {

    //@EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        try {
            patternFilter();
        } catch (CompileErrorException e) {
            throw new RuntimeException(e);
        }
    }

    private void patternFilter() throws CompileErrorException {
        List<Pattern> patterns = Arrays.asList(
                Pattern.compile("The number is ([0-9]+)", Pattern.CASE_INSENSITIVE),
                Pattern.compile("The color is (blue|red|orange)")
                // and thousands more
        );

        //not thread-safe, create per thread
        PatternFilter filter = new PatternFilter(patterns);

        //this list now only contains the probably matching patterns, in this case the first one
        List<Matcher> matchers = filter.filter("The number is 7 the NUMber is 27");

        //now we use the regular java regex api to check for matches - this is not hyperscan specific
        for(Matcher matcher : matchers) {
            while (matcher.find()) {
                // will print 7 and 27
                System.out.println(matcher.group(1));
            }
        }


    }
}
