package org.g4studio.core.net.ftp.parser;

import java.text.ParseException;
import java.util.Calendar;

import org.g4studio.core.net.ftp.Configurable;
import org.g4studio.core.net.ftp.FTPClientConfig;


/**
 * <p>
 * This abstract class implements the common timestamp parsing
 * algorithm for all the concrete parsers.  Classes derived from
 * this one will parse file listings via a supplied regular expression
 * that pulls out the date portion as a separate string which is
 * passed to the underlying {@link FTPTimestampParser delegate} to
 * handle parsing of the file timestamp.
 * </p><p>
 * This class also implements the {@link Configurable Configurable}
 * interface to allow the parser to be configured from the outside.
 * </p>
 * @since 1.4
 */
/**
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates - Comments
 */
public abstract class ConfigurableFTPFileEntryParserImpl 
extends RegexFTPFileEntryParserImpl 
implements Configurable 
{

	private FTPTimestampParser timestampParser;
	
	/**
	 * Only constructor for this absract class.
	 * @param regex  Regular expression used main parsing of the 
	 * file listing.
	 */
	public ConfigurableFTPFileEntryParserImpl(String regex)
	{
		super(regex);
		this.timestampParser = new FTPTimestampParserImpl();
	}

	/**
	 * This method is called by the concrete parsers to delegate
	 * timestamp parsing to the timestamp parser.
	 * <p>
	 * @param timestampStr the timestamp string pulled from the 
	 * file listing by the regular expression parser, to be submitted
	 * to the <code>timestampParser</code> for extracting the timestamp.
	 * @return a <code>java.util.Calendar</code> containing results of the 
	 * timestamp parse. 
	 */
	public Calendar parseTimestamp(String timestampStr) throws ParseException {
		return this.timestampParser.parseTimestamp(timestampStr);
	}


	/**
	 * Implementation of the {@link  Configurable  Configurable}
	 * interface. Configures this parser by delegating to the
	 * underlying Configurable FTPTimestampParser implementation, '
	 * passing it the supplied {@link  FTPClientConfig FTPClientConfig}
	 * if that is non-null or a default configuration defined by
	 * each concrete subclass.
	 * </p> 
	 * @param config the configuration to be used to configure this parser.
	 * If it is null, a default configuration defined by
	 * each concrete subclass is used instead.
	 */
    public void configure(FTPClientConfig config)
    {
        if (this.timestampParser instanceof Configurable) {
            FTPClientConfig defaultCfg = getDefaultConfiguration();
	        if (config != null) {
	            if (null == config.getDefaultDateFormatStr()) {
	                config.setDefaultDateFormatStr(defaultCfg.getDefaultDateFormatStr());
	            }
	            if (null == config.getRecentDateFormatStr()) {
	                config.setRecentDateFormatStr(defaultCfg.getRecentDateFormatStr());
	            }
	            ((Configurable)this.timestampParser).configure(config);
	        } else {
	            ((Configurable)this.timestampParser).configure(defaultCfg);
	        }
        }
    }
    
    /**
     * Each concrete subclass must define this member to create
     * a default configuration to be used when that subclass is
     * instantiated without a {@link  FTPClientConfig  FTPClientConfig}
     * parameter being specified.
     * @return the default configuration for the subclass.
     */
    protected abstract FTPClientConfig getDefaultConfiguration();
}
